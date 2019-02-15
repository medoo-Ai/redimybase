/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flowable.app.rest.editor;

import java.io.InputStream;
import java.text.ParseException;

import com.aispread.manager.flowable.entity.ActReProcdefEntity;
import com.aispread.manager.flowable.entity.FlowDefinitionEntity;
import com.aispread.manager.flowable.service.ActReProcdefService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redimybase.flowable.controller.FlowDefinitionController;
import com.redimybase.framework.bean.R;
import com.redimybase.security.utils.SecurityUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.flowable.app.domain.editor.Model;
import org.flowable.app.model.editor.ModelKeyRepresentation;
import org.flowable.app.model.editor.ModelRepresentation;
import org.flowable.app.repository.editor.ModelRepository;
import org.flowable.app.security.SecurityUtils;
import org.flowable.app.service.api.ModelService;
import org.flowable.app.service.exception.BadRequestException;
import org.flowable.app.service.exception.ConflictingRequestException;
import org.flowable.app.service.exception.InternalServerErrorException;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.editor.language.json.converter.BpmnJsonConverter;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.idm.api.User;
import org.flowable.idm.engine.impl.persistence.entity.UserEntityImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
@RequestMapping("/flow-moduler/app")
public class ModelResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelResource.class);

    private static final String RESOLVE_ACTION_OVERWRITE = "overwrite";
    private static final String RESOLVE_ACTION_SAVE_AS = "saveAs";
    private static final String RESOLVE_ACTION_NEW_VERSION = "newVersion";

    @Autowired
    protected ModelService modelService;

    @Autowired
    protected ModelRepository modelRepository;

    @Autowired
    protected RepositoryService repositoryService;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private ModelsResource modelsResource;

    @Autowired
    private FlowDefinitionController flowDefinitionController;

    @Autowired
    private ActReProcdefService actReProcdefService;

    protected BpmnJsonConverter bpmnJsonConverter = new BpmnJsonConverter();

    protected BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();

    /**
     * GET /rest/models/{modelId} -> Get process model
     */
    @RequestMapping(value = "/rest/models/{modelId}", method = RequestMethod.GET, produces = "application/json")
    public ModelRepresentation getModel(@PathVariable String modelId) {
        return modelService.getModelRepresentation(modelId);
    }

    /**
     * GET /rest/models/{modelId}/thumbnail -> Get process model thumbnail
     */
    @RequestMapping(value = "/rest/models/{modelId}/thumbnail", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getModelThumbnail(@PathVariable String modelId) {
        Model model = modelService.getModel(modelId);
        return model.getThumbnail();
    }

    /**
     * PUT /rest/models/{modelId} -> update process model properties
     */
    @RequestMapping(value = "/rest/models/{modelId}", method = RequestMethod.PUT)
    public ModelRepresentation updateModel(@PathVariable String modelId, @RequestBody ModelRepresentation updatedModel) {
        // Get model, write-permission required if not a favorite-update
        Model model = modelService.getModel(modelId);

        ModelKeyRepresentation modelKeyInfo = modelService.validateModelKey(model, model.getModelType(), updatedModel.getKey());
        if (modelKeyInfo.isKeyAlreadyExists()) {
            throw new BadRequestException("Model with provided key already exists " + updatedModel.getKey());
        }

        try {
            updatedModel.updateModel(model);

            if (model.getModelType() != null) {
                ObjectNode modelNode = (ObjectNode) objectMapper.readTree(model.getModelEditorJson());
                modelNode.put("name", model.getName());
                modelNode.put("key", model.getKey());

                if (Model.MODEL_TYPE_BPMN == model.getModelType()) {
                    ObjectNode propertiesNode = (ObjectNode) modelNode.get("properties");
                    propertiesNode.put("process_id", model.getKey());
                    propertiesNode.put("name", model.getName());
                    if (StringUtils.isNotEmpty(model.getDescription())) {
                        propertiesNode.put("documentation", model.getDescription());
                    }
                    modelNode.set("properties", propertiesNode);
                }
                model.setModelEditorJson(modelNode.toString());
            }

            modelRepository.save(model);

            ModelRepresentation result = new ModelRepresentation(model);
            return result;

        } catch (Exception e) {
            throw new BadRequestException("Model cannot be updated: " + modelId);
        }
    }

    /**
     * DELETE /rest/models/{modelId} -> delete process model or, as a non-owner, remove the share info link for that user specifically
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/rest/models/{modelId}", method = RequestMethod.DELETE)
    public void deleteModel(@PathVariable String modelId) {

        // Get model to check if it exists, read-permission required for delete
        Model model = modelService.getModel(modelId);

        try {
            modelService.deleteModel(model.getId());

        } catch (Exception e) {
            LOGGER.error("Error while deleting: ", e);
            throw new BadRequestException("Model cannot be deleted: " + modelId);
        }
    }

    /**
     * GET /rest/models/{modelId}/editor/json -> get the JSON model
     */
    @RequestMapping(value = "/rest/models/{modelId}/editor/json", method = RequestMethod.GET, produces = "application/json")
    public ObjectNode getModelJSON(@PathVariable String modelId) {
        Model model = modelService.getModel(modelId);
        ObjectNode modelNode = objectMapper.createObjectNode();
        modelNode.put("modelId", model.getId());
        modelNode.put("name", model.getName());
        modelNode.put("key", model.getKey());
        modelNode.put("description", model.getDescription());
        modelNode.putPOJO("lastUpdated", model.getLastUpdated());
        modelNode.put("lastUpdatedBy", model.getLastUpdatedBy());
        if (StringUtils.isNotEmpty(model.getModelEditorJson())) {
            try {
                ObjectNode editorJsonNode = (ObjectNode) objectMapper.readTree(model.getModelEditorJson());
                editorJsonNode.put("modelType", "model");
                modelNode.set("model", editorJsonNode);
            } catch (Exception e) {
                LOGGER.error("Error reading editor json {}", modelId, e);
                throw new InternalServerErrorException("Error reading editor json " + modelId);
            }

        } else {
            ObjectNode editorJsonNode = objectMapper.createObjectNode();
            editorJsonNode.put("id", "canvas");
            editorJsonNode.put("resourceId", "canvas");
            ObjectNode stencilSetNode = objectMapper.createObjectNode();
            stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
            editorJsonNode.put("modelType", "model");
            modelNode.set("model", editorJsonNode);
        }
        return modelNode;
    }

    /**
     * 保存流程模型
     */
    @RequestMapping(value = "/rest/models/{modelId}/{deployment}/editor/json", method = RequestMethod.POST)
    @ApiOperation(value = "保存流程模型")
    public R<?> saveModel(@ApiParam(value = "流程模型ID", required = true) @PathVariable String modelId,
                          @ApiParam(value = "是否部署(0:否,1:是)", required = true) @PathVariable Integer deployment,
                          @RequestBody MultiValueMap<String, String> values) {

        saveModel(modelId, values);
        Deployment deploy = null;
        if (deployment == 1) {
            Model model = modelService.getModel(modelId);
             deploy = repositoryService.createDeployment().addBpmnModel(
                    model.getKey() + ".bpmn",
                    modelService.getBpmnModel(model)).deploy();
        }
        if (deploy == null) {
            return R.fail("未知错误");
        }

        ActReProcdefEntity actReProcdefEntity = actReProcdefService.getOne(new QueryWrapper<ActReProcdefEntity>().eq("DEPLOYMENT_ID_", deploy.getId()).select("ID_,VERSION_,KEY_"));
        FlowDefinitionEntity flowDefinitionEntity = JSONObject.parseObject(values.get("definitionEntity").get(0), FlowDefinitionEntity.class);

        flowDefinitionEntity.setName(values.get("name").get(0));
        flowDefinitionEntity.setDefinitionKey(actReProcdefEntity.getKey());
        flowDefinitionEntity.setFlowDefinitionKey(actReProcdefEntity.getKey());
        flowDefinitionEntity.setFlowDefinitionVersion(String.valueOf(actReProcdefEntity.getVersion()));
        flowDefinitionEntity.setFlowDefinitionId(actReProcdefEntity.getId());
        flowDefinitionController.save(flowDefinitionEntity);
        return R.ok();
    }

    /**
     * POST /rest/models/{modelId}/editor/json -> save the JSON model
     */
    @RequestMapping(value = "/rest/models/{modelId}/editor/json", method = RequestMethod.POST)
    public ModelRepresentation saveModel(@PathVariable String modelId, @RequestBody MultiValueMap<String, String> values) {

        if (SecurityUtils.getCurrentUserObject() == null) {
            //为流程模型提供创建者
            User user = new UserEntityImpl();
            user.setId("admin");
            SecurityUtils.assumeUser(user);
        }

        // Validation: see if there was another update in the meantime
        long lastUpdated = System.currentTimeMillis();

        Model model = modelService.getModel(modelId);
        User currentUser = SecurityUtils.getCurrentUserObject();
        boolean currentUserIsOwner = model.getLastUpdatedBy().equals(currentUser.getId());
        String resolveAction = values.getFirst("conflictResolveAction");

        // If timestamps differ, there is a conflict or a conflict has been resolved by the user
        if (model.getLastUpdated().getTime() != lastUpdated) {

            if (RESOLVE_ACTION_SAVE_AS.equals(resolveAction)) {

                String saveAs = values.getFirst("saveAs");
                String json = values.getFirst("json_xml");
                return createNewModel(saveAs, model.getDescription(), model.getModelType(), json);

            } else if (RESOLVE_ACTION_OVERWRITE.equals(resolveAction)) {
                return updateModel(model, values, false);
            } else if (RESOLVE_ACTION_NEW_VERSION.equals(resolveAction)) {
                return updateModel(model, values, true);
            } else {

                // Exception case: the user is the owner and selected to create a new version
                String isNewVersionString = values.getFirst("newversion");
                //String isNewVersionString = "true";
                if (currentUserIsOwner) {
                    //&& "true".equals(isNewVersionString)
                    return updateModel(model, values, true);
                } else {
                    // Tried everything, this is really a conflict, return 409
                    ConflictingRequestException exception = new ConflictingRequestException("Process model was updated in the meantime");
                    exception.addCustomData("userFullName", "vim");
                    exception.addCustomData("userFullName", model.getLastUpdatedBy());
                    //exception.addCustomData("newVersionAllowed", currentUserIsOwner);
                    throw exception;
                }
            }

        } else {

            // Actual, regular, update
            return updateModel(model, values, false);

        }

    }

    /**
     * POST /rest/models/{modelId}/editor/newversion -> create a new model version
     */
    @RequestMapping(value = "/rest/models/{modelId}/newversion", method = RequestMethod.POST)
    public ModelRepresentation importNewVersion(@PathVariable String modelId, @RequestParam("file") MultipartFile file) {
        InputStream modelStream = null;
        try {
            modelStream = file.getInputStream();
        } catch (Exception e) {
            throw new BadRequestException("Error reading file inputstream", e);
        }

        return modelService.importNewVersion(modelId, file.getOriginalFilename(), modelStream);
    }

    protected ModelRepresentation updateModel(Model model, MultiValueMap<String, String> values, boolean forceNewVersion) {

        String name = values.getFirst("name");
        String key = values.getFirst("key");
        String description = values.getFirst("description");
        String isNewVersionString = values.getFirst("newversion");
        String newVersionComment = null;

        ModelKeyRepresentation modelKeyInfo = modelService.validateModelKey(model, model.getModelType(), key);
        if (modelKeyInfo.isKeyAlreadyExists()) {
            throw new BadRequestException("Model with provided key already exists " + key);
        }

        boolean newVersion = false;
        if (forceNewVersion) {
            newVersion = true;
            newVersionComment = values.getFirst("comment");
        } else {
            if (isNewVersionString != null) {
                newVersion = "true".equals(isNewVersionString);
                newVersionComment = values.getFirst("comment");
            }
        }

        String json = values.getFirst("json_xml");

        try {
            model = modelService.saveModel(model.getId(), name, key, description, json, newVersion,
                    newVersionComment, SecurityUtils.getCurrentUserObject());
            return new ModelRepresentation(model);

        } catch (Exception e) {
            LOGGER.error("Error saving model {}", model.getId(), e);
            throw new BadRequestException("Process model could not be saved " + model.getId());
        }
    }

    protected ModelRepresentation createNewModel(String name, String description, Integer modelType, String editorJson) {
        ModelRepresentation model = new ModelRepresentation();
        model.setName(name);
        model.setDescription(description);
        model.setModelType(modelType);
        Model newModel = modelService.createModel(model, editorJson, SecurityUtils.getCurrentUserObject());
        return new ModelRepresentation(newModel);
    }
}
