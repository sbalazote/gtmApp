package com.lsntsolutions.gtmApp.controllers.printing;

import com.lsntsolutions.gtmApp.constant.AuditState;
import com.lsntsolutions.gtmApp.constant.RoleOperation;
import com.lsntsolutions.gtmApp.dto.PrinterResultDTO;
import com.lsntsolutions.gtmApp.helper.impl.printer.OrderLabelPrinter;
import com.lsntsolutions.gtmApp.model.Order;
import com.lsntsolutions.gtmApp.service.AuditService;
import com.lsntsolutions.gtmApp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OrderLabelController {

    @Autowired
    private AuditService auditService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderLabelPrinter orderLabelPrinter;

    @RequestMapping(value = "/printOrderLabel", method = RequestMethod.POST)
    public @ResponseBody
    PrinterResultDTO printOrderLabel(@RequestParam Integer orderId) throws Exception {
        Order order = this.orderService.get(orderId);
        PrinterResultDTO printerResultDTO = new PrinterResultDTO(order.getProvisioningRequest().getFormatId());
        this.orderLabelPrinter.print(order, printerResultDTO);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            this.auditService.addAudit(auth.getName(), RoleOperation.ORDER_ASSEMBLY.getId(), AuditState.COMFIRMED, order.getId());
        }
        return printerResultDTO;
    }
}