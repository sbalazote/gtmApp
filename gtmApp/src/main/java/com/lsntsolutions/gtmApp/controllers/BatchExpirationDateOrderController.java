package com.lsntsolutions.gtmApp.controllers;

import com.lsntsolutions.gtmApp.form.OrderAssemblyForm;
import com.lsntsolutions.gtmApp.form.SearchProvisioningForm;
import com.lsntsolutions.gtmApp.model.*;
import com.lsntsolutions.gtmApp.service.OrderService;
import com.lsntsolutions.gtmApp.service.ProvisioningRequestService;
import com.lsntsolutions.gtmApp.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by a983060 on 29/12/2015.
 */
@Controller
@SessionAttributes({"provisioningDetailsToAssign","orderDetailsMap","provisioningRequestDetailsMap", "stockInUse"})
public class BatchExpirationDateOrderController {
    @Autowired
    private ProvisioningRequestService provisioningRequestService;
    @Autowired
    private StockService stockService;
    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/batchExpirationDateOrderAssemblySearch", method = RequestMethod.GET)
    public String batchExpirationDateOrderAssemblySearch(ModelMap modelMap) throws Exception {
        return "batchExpirationDateOrderAssemblySearch";
    }

    @RequestMapping(value = "/searchProvisioningById", method = RequestMethod.POST)
    public ModelAndView searchProvisioningById(ModelMap modelMap, SearchProvisioningForm searchProvisioningForm){
        ModelAndView modelAndView = new ModelAndView();
        ProvisioningRequest provisioningRequest = this.provisioningRequestService.get(searchProvisioningForm.getProvisioningRequestId());
        HashMap<Integer, Integer> provisioningDetailsToAssign = new HashMap<>();
        HashMap<Integer, List<OrderDetail>> orderDetailsMap = new HashMap<>();
        HashMap<Integer, ProvisioningRequestDetail> provisioningRequestDetailsMap = new HashMap<>();

        for(ProvisioningRequestDetail provisioningRequestDetail : provisioningRequest.getProvisioningRequestDetails()){
            provisioningDetailsToAssign.put(provisioningRequestDetail.getId(), provisioningRequestDetail.getAmount());
            orderDetailsMap.put(provisioningRequestDetail.getId(), new ArrayList<OrderDetail>());
            provisioningRequestDetailsMap.put(provisioningRequestDetail.getId(),provisioningRequestDetail);
        }
        modelAndView.addObject("provisioningDetailsToAssign", provisioningDetailsToAssign);
        modelAndView.addObject("orderDetailsMap", orderDetailsMap);
        modelAndView.addObject("provisioningRequestDetailsMap", provisioningRequestDetailsMap);
        modelAndView.addObject("stockInUse", new ArrayList<>());
        modelMap.put("provisioningRequestId", searchProvisioningForm.getProvisioningRequestId());
        modelAndView.setViewName("redirect:batchExpirationDateOrderAssembly.do");

        return modelAndView;
    }

    @RequestMapping(value = "/batchExpirationDateOrderAssembly", method = RequestMethod.GET)
    public String batchExpirationDateOrderAssembly(ModelMap modelMap, @RequestParam Integer provisioningRequestId, OrderAssemblyForm orderAssemblyForm, HttpServletRequest request) throws Exception {
        modelMap.put("provisioningRequestId", provisioningRequestId);
        ProvisioningRequest provisioningRequest = this.provisioningRequestService.get(provisioningRequestId);
        HashMap<Integer, Integer> provisioningDetailsToAssign = (HashMap<Integer, Integer>) request.getSession().getAttribute("provisioningDetailsToAssign");
        HashMap<Integer, ProvisioningRequestDetail> provisioningRequestDetailsMap = (HashMap<Integer, ProvisioningRequestDetail>) request.getSession().getAttribute("provisioningRequestDetailsMap");
        HashMap<Integer, List<OrderDetail>> orderDetailsMap = (HashMap<Integer, List<OrderDetail>>) request.getSession().getAttribute("orderDetailsMap");
        List<Integer> stockInUse = (List<Integer>) request.getSession().getAttribute("stockInUse");
        if(orderAssemblyForm.getStockAmountInput() != null && Integer.valueOf(orderAssemblyForm.getStockAmountInput()) <= (orderAssemblyForm.getAmount() - orderAssemblyForm.getAssignAmount())){
            List<OrderDetail> orderDetails = orderDetailsMap.get(orderAssemblyForm.getProvisioningRequestDetailId());
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setAmount(Integer.valueOf(orderAssemblyForm.getStockAmountInput()));
            Stock stock = this.stockService.get(Integer.valueOf(orderAssemblyForm.getStockInput()));
            orderDetail.setBatch(stock.getBatch());
            orderDetail.setExpirationDate(stock.getExpirationDate());
            orderDetail.setGtin(stock.getGtin());
            orderDetail.setProduct(stock.getProduct());
            orderDetails.add(orderDetail);
            stockInUse.add(stock.getId());
            orderDetailsMap.put(orderAssemblyForm.getProvisioningRequestDetailId(),orderDetails);
        }
        modelMap.put("amountAssign", 0);
        boolean finished = true;
        for(Integer provisioningDetailId : provisioningDetailsToAssign.keySet()){
            ProvisioningRequestDetail provisioningRequestDetail = provisioningRequestDetailsMap.get(provisioningDetailId);
            modelMap.put("provisioningRequestDetailId", provisioningDetailId);
            Integer amountAssign = countTotalAssign(orderDetailsMap.get(provisioningDetailId));
            if(amountAssign < provisioningDetailsToAssign.get(provisioningDetailId)){
                modelMap.put("stock", filterStockInUse(this.stockService.getBatchExpirationDateStock(provisioningRequestDetail.getProduct().getId(), provisioningRequest.getAgreement().getId()),stockInUse));
                modelMap.put("productDescription", provisioningRequestDetail.getProduct().getCode() + " - " + provisioningRequestDetail.getProduct().getDescription());
                modelMap.put("amountAssign", amountAssign);
                modelMap.put("amount", provisioningRequestDetail.getAmount());
                finished = false;
                break;
            }
        }
        if(finished){
            Order order = new Order();
            order.setProvisioningRequest(provisioningRequest);
            List<OrderDetail> orderDetails = new ArrayList<>();
            for(Integer provisioningDetailId : orderDetailsMap.keySet()){
                orderDetails.addAll(orderDetailsMap.get(provisioningDetailId));
            }
            order.setOrderDetails(orderDetails);
            order.setCancelled(false);
            this.orderService.save(order);
            return "batchExpirationDateOrderAssemblySearch";
        }
        return "batchExpirationDateOrderAssembly";
    }

    private Integer countTotalAssign(List<OrderDetail> orderDetails){
        Integer amount = 0;
        if(orderDetails != null) {
            for (OrderDetail orderDetail : orderDetails) {
                amount += orderDetail.getAmount();
            }
        }
        return amount;
    }

    private List<Stock> filterStockInUse(List<Stock> stocks, List<Integer> stockInUse){
        List<Stock> stock = new ArrayList<>();
        for(Stock st : stocks){
            boolean found = false;
            for(Integer id : stockInUse){
                if(id == st.getId()){
                    found = true;
                }
            }
            if(!found){
                stock.add(st);
            }
        }
        return stock;
    }

}

