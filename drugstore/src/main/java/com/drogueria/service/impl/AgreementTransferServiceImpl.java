package com.drogueria.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drogueria.constant.AuditState;
import com.drogueria.constant.RoleOperation;
import com.drogueria.dto.AgreementTransferDTO;
import com.drogueria.dto.AgreementTransferDetailDTO;
import com.drogueria.model.Agreement;
import com.drogueria.model.AgreementTransfer;
import com.drogueria.model.AgreementTransferDetail;
import com.drogueria.model.Product;
import com.drogueria.model.ProductGtin;
import com.drogueria.persistence.dao.AgreementTransferDAO;
import com.drogueria.service.AgreementService;
import com.drogueria.service.AgreementTransferService;
import com.drogueria.service.AuditService;
import com.drogueria.service.ProductGtinService;
import com.drogueria.service.ProductService;
import com.drogueria.service.StockService;

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
		this.auditService.addAudit(name, RoleOperation.AGREEMENT_TRANSFER.getId(), AuditState.COMFIRMED, agreementTransfer.getId());
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
					destinationAgreement);
			details.add(agreementTransferDetail);
		}
		agreementTransfer.setAgreementTransferDetail(details);
		return agreementTransfer;
	}
}
