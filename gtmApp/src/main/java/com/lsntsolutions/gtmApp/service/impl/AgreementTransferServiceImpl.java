package com.lsntsolutions.gtmApp.service.impl;

import com.lsntsolutions.gtmApp.constant.RoleOperation;
import com.lsntsolutions.gtmApp.dto.AgreementTransferDTO;
import com.lsntsolutions.gtmApp.dto.AgreementTransferDetailDTO;
import com.lsntsolutions.gtmApp.model.*;
import com.lsntsolutions.gtmApp.persistence.dao.AgreementTransferDAO;
import com.lsntsolutions.gtmApp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AgreementTransferServiceImpl implements AgreementTransferService {

	@Autowired
	private AgreementTransferDAO agreementTransferDAO;
	@Autowired
	private AgreementService agreementService;
	@Autowired
	private ProductService productService;
	@Autowired
	private StockService stockService;
	@Autowired
	private ProductGtinService productGtinService;
	@Autowired
	private AuditService auditService;

	@Override
	public void save(AgreementTransfer agreementTransfer) {
		this.agreementTransferDAO.save(agreementTransfer);
	}

	@Override
	public AgreementTransfer get(Integer id) {
		return this.agreementTransferDAO.get(id);
	}

	@Override
	public List<AgreementTransfer> getAll() {
		return this.agreementTransferDAO.getAll();
	}

	@Override
	public void updateAgreementStock(AgreementTransferDTO agreementTransferDTO, String name) throws ParseException {
		AgreementTransfer agreementTransfer = this.buildInput(agreementTransferDTO);
		this.agreementTransferDAO.save(agreementTransfer);
		this.auditService.addAudit(name, RoleOperation.AGREEMENT_TRANSFER.getId(), agreementTransfer.getId());
	}

	private AgreementTransfer buildInput(AgreementTransferDTO agreementTransferDTO) throws ParseException {
		SimpleDateFormat expirationDateFormatter = new SimpleDateFormat("dd/MM/yy");

		Agreement originAgreement = this.agreementService.get(agreementTransferDTO.getOriginAgreementId());
		Agreement destinationAgreement = this.agreementService.get(agreementTransferDTO.getDestinationAgreementId());

		AgreementTransfer agreementTransfer = new AgreementTransfer();
		agreementTransfer.setOriginAgreement(originAgreement);
		agreementTransfer.setDestinationAgreement(destinationAgreement);
		Product product = null;
		List<AgreementTransferDetail> details = new ArrayList<>();
		for (AgreementTransferDetailDTO agreementTransferDetailDTO : agreementTransferDTO.getAgreementTransferDetails()) {
			if (product == null || !product.getId().equals(agreementTransferDetailDTO.getProductId())) {
				product = this.productService.get(agreementTransferDetailDTO.getProductId());
			}

			AgreementTransferDetail agreementTransferDetail = new AgreementTransferDetail();
			agreementTransferDetail.setAmount(agreementTransferDetailDTO.getAmount());
			agreementTransferDetail.setBatch(agreementTransferDetailDTO.getBatch());
			if (agreementTransferDetailDTO.getExpirationDate() != null && !agreementTransferDetailDTO.getExpirationDate().isEmpty()) {
				agreementTransferDetail.setExpirationDate(expirationDateFormatter.parse(agreementTransferDetailDTO.getExpirationDate()));
			}
			agreementTransferDetail.setSerialNumber(agreementTransferDetailDTO.getSerialNumber());

			if (agreementTransferDetailDTO.getGtin() != null) {

				ProductGtin productGtin = this.productGtinService.getByNumber(agreementTransferDetailDTO.getGtin());
				agreementTransferDetail.setGtin(productGtin);
			} else {
				if (product.getLastProductGtin() != null) {
					agreementTransferDetail.setGtin(product.getLastProductGtin());
				}
			}

			agreementTransferDetail.setProduct(product);
			String gtin = null;
			if (agreementTransferDetail.getGtin() != null) {
				gtin = agreementTransferDetail.getGtin().getNumber();
			}
			this.stockService.updateAgreementStock(agreementTransferDetail.getProduct().getId(), agreementTransferDetail.getSerialNumber(), gtin,
					originAgreement, destinationAgreement);
			details.add(agreementTransferDetail);
		}
		agreementTransfer.setAgreementTransferDetail(details);
		return agreementTransfer;
	}
}
