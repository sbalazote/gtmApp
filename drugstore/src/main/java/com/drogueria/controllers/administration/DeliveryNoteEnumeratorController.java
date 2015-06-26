package com.drogueria.controllers.administration;

import com.drogueria.dto.AgentDTO;
import com.drogueria.dto.DeliveryNoteEnumeratorDTO;
import com.drogueria.model.Agent;
import com.drogueria.model.DeliveryNoteEnumerator;
import com.drogueria.service.DeliveryNoteEnumeratorService;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class DeliveryNoteEnumeratorController {

    @Autowired
    private DeliveryNoteEnumeratorService deliveryNoteEnumeratorService;

    @RequestMapping(value = "/deliveryNoteEnumeratorAdministration", method = RequestMethod.GET)
    public String deliveryNoteEnumeratorAdministration(ModelMap modelMap) throws Exception {
        return "deliveryNoteEnumeratorAdministration";
    }

    @RequestMapping(value = "/saveDeliveryNoteEnumerator", method = RequestMethod.POST)
    public @ResponseBody DeliveryNoteEnumerator saveDeliveryNoteEnumerator(@RequestBody DeliveryNoteEnumeratorDTO deliveryNoteEnumeratorDTO) throws Exception {
        DeliveryNoteEnumerator deliveryNoteEnumerator = this.buildModel(deliveryNoteEnumeratorDTO);
        this.deliveryNoteEnumeratorService.save(deliveryNoteEnumerator);
        return deliveryNoteEnumerator;
    }

    private DeliveryNoteEnumerator buildModel(DeliveryNoteEnumeratorDTO deliveryNoteEnumeratorDTO) {
        DeliveryNoteEnumerator deliveryNoteEnumerator = new DeliveryNoteEnumerator();
        if (deliveryNoteEnumeratorDTO.getId() != null) {
            deliveryNoteEnumerator.setId(deliveryNoteEnumeratorDTO.getId());
        }
        deliveryNoteEnumerator.setLastDeliveryNoteNumber(deliveryNoteEnumeratorDTO.getLastDeliveryNoteNumber());
        deliveryNoteEnumerator.setDeliveryNotePOS(deliveryNoteEnumeratorDTO.getDeliveryNotePOS());
        deliveryNoteEnumerator.setFake(deliveryNoteEnumeratorDTO.isFake());
        deliveryNoteEnumerator.setActive(deliveryNoteEnumeratorDTO.isActive());
        return deliveryNoteEnumerator;
    }

    @RequestMapping(value = "/readDeliveryNoteEnumerator", method = RequestMethod.GET)
    public @ResponseBody DeliveryNoteEnumerator readDeliveryNoteEnumerator(@RequestParam Integer deleteDeliveryNoteEnumeratorId) throws Exception {
        return this.deliveryNoteEnumeratorService.get(deleteDeliveryNoteEnumeratorId);
    }

    @RequestMapping(value = "/deleteDeliveryNoteEnumerator", method = RequestMethod.POST)
    public @ResponseBody boolean deleteDeliveryNoteEnumerator(@RequestParam Integer deleteDeliveryNoteEnumeratorId) throws Exception {
        return this.deliveryNoteEnumeratorService.delete(deleteDeliveryNoteEnumeratorId);
    }

    @RequestMapping(value = "/existsDeliveryNoteEnumerator", method = RequestMethod.GET)
    public @ResponseBody Boolean existsAgent(@RequestParam Integer deliveryNotePOS, Boolean fake) throws Exception {
        return this.deliveryNoteEnumeratorService.exists(deliveryNotePOS,fake);
    }

    @RequestMapping(value = "/getMatchedDeliveryNoteEnumerators", method = RequestMethod.POST)
    public @ResponseBody
    String getMatchedDeliveryNoteEnumerators(@RequestParam Map<String, String> parametersMap) throws JSONException {

        String searchPhrase = parametersMap.get("searchPhrase");
        Integer current = Integer.parseInt(parametersMap.get("current"));
        Integer rowCount = Integer.parseInt(parametersMap.get("rowCount"));

        JSONArray jsonArray = new JSONArray();
        int start = (current - 1) * rowCount;
        int length = rowCount;
        long total;

        List<DeliveryNoteEnumerator> deliveryNoteEnumerators = null;
        if (searchPhrase.matches("")) {
            deliveryNoteEnumerators = this.deliveryNoteEnumeratorService.getPaginated(start, length);
            total = this.deliveryNoteEnumeratorService.getTotalNumber();
        } else {
            deliveryNoteEnumerators = this.deliveryNoteEnumeratorService.getForAutocomplete(searchPhrase, null,null);
            total = deliveryNoteEnumerators.size();
            if (total < start + length) {
                deliveryNoteEnumerators = deliveryNoteEnumerators.subList(start, (int) total);
            } else {
                deliveryNoteEnumerators = deliveryNoteEnumerators.subList(start, start + length);
            }
        }

        for (DeliveryNoteEnumerator agent : deliveryNoteEnumerators) {
            JSONObject dataJson = new JSONObject();

            dataJson.put("id", agent.getId());
            dataJson.put("deliveryNotePOS", agent.getDeliveryNotePOS());
            dataJson.put("lastDeliveryNoteNumber", agent.getLastDeliveryNoteNumber());
            dataJson.put("isActive", agent.isActive() == true ? "Si" : "No");
            dataJson.put("isFake", agent.isFake() == true ? "Si" : "No");
            jsonArray.put(dataJson);
        }

        JSONObject responseJson = new JSONObject();
        responseJson.put("current", current);
        responseJson.put("rowCount", (total < (start + length)) ? (total - length) : length);
        responseJson.put("rows", jsonArray);
        responseJson.put("total", total);

        return responseJson.toString();
    }
}
