//package tz.go.tcra.lims.intergration.temp;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.configurationprocessor.json.JSONObject;
//import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.RequestBody;
////import tz.go.ega.signature.crypto.RSA;
////import tz.go.ega.signature.dtos.CryptoData;
//import tz.go.tcra.lims.utils.Response;
//
//import java.io.IOException;
//import java.security.PrivateKey;
//import java.security.PublicKey;
//import java.time.LocalDate;
//import java.util.Collections;
//
//@Slf4j
//@Service
//public class BillServiceImplementation implements BillService {
//
//    @Value("${billing.spcode}")
//    private String SP_CODE;
//
//    @Value("${billing.subspcode}")
//    private String SUB_SP_CODE;
//
//    @Autowired
//    private RSA rsaService;
//
//    @Autowired
//    private CryptoConfig cryptoConfig;
//
////    @Autowired
////    private ERMSBillingRepository ermsBillingRepo;
////
////
////    @Autowired
////    private BillRepository billRepository;
////
////    @Autowired
////    private BillItemRepository billItemRepository;
////
////    @Autowired
////    private LoggedUser loggedUser;
////
////    @Autowired
////    private UaaService uaaService;
////
////    @Autowired
////    private BillDtRepository billDtRepository;
////
////    @Autowired
////    private ServiceAttachmentService serviceAttachmentService;
//
//
////    @Override
////    public Response<Bill> requestBillFromERMS(Bill bill) {
////
////        return null;
////    }
//
//    @Override
//    public Response<Bill> generateBill(long billId) {
//        log.info("Attempt to send bill to erms: {}", billId);
////        Bill bill = billRepository.getOne(billId);
//        try {
//            BillDto billDto = new BillDto();
//            billDto.setBillDate(LocalDate.now().toString());
//            billDto.setBillType("NORMAL");
//            billDto.setModeOfSettlement("bill.getModeOfSettlement().toString()");
//            billDto.setSourceRef("");
//            billDto.setDescription("bill.getDescription().");
//
////            ClientDto client = createClient(Long.valueOf(bill.getClientCode()));
////
////            billDto.setClient(client);
////            billDto.setItems(createBillItemList(bill));
//
//            BillData billData = new BillData();
//            billData.setBills(Collections.singletonList(billDto));
//            billData.setGeneratedBy("admin");
//            billData.setSpCode(SP_CODE);
//            billData.setSubSpCode(SUB_SP_CODE);
//
//            CryptoData<BillData> sendBill = generateSignedData(billData);
//            log.info(sendBill.getData() + " " + sendBill.getSignature());
//
//            ObjectMapper mapper = new ObjectMapper();
//            String billDataStr = mapper.writeValueAsString(sendBill);
//            log.info(billDataStr);
//
//            String billResponse = null;//ermsBillingRepo.sendBill(sendBill);
//
//            log.info("bill response: " + billResponse);
//            CryptoData<String> receiveBill = processSignedData(billResponse);
//
////            GispHelper.print(receiveBill);
//            String billDataKey = "billData";
//            String billDataResp = mapper.writeValueAsString(receiveBill.getData());
//            JSONObject jsonObj = new JSONObject();
//            jsonObj.put(billDataKey, billDataResp);
//            log.info("Data: {}", jsonObj.get(billDataKey));
//
//            ERMSBillResponse response = mapper.readValue(jsonObj.get(billDataKey).toString(), ERMSBillResponse.class);
//
////            Bill currentBill = findByUid(response.getBills().get(0).getSourceRef());
//            Bill currentBill = null;
//            if (currentBill != null) {
//                log.info("request-ref: {}", response.getBills().get(0).getRequestRef());
////                currentBill.setBillNumber(response.getBills().get(0).getBillNumber());
////                Bill savedBill = billRepository.save(currentBill);
//
////                return new Response<>(true, ResponseCode.SUCCESS, savedBill);
//            }
//
//            log.info(response.getBills().get(0).getBillNumber());
////            return new Response<>(false, ResponseCode.FAILURE, null);
//        } catch (Exception e) {
//            log.error("exception occurred: {}", e);
//        }
//
////        return new Response<>(false, ResponseCode.FAILURE, null);
//        return null;
//    }
//
//    public CryptoData<BillData> generateSignedData(BillData sampleUserData) throws Exception {
//        CryptoData<BillData> cryptoData = new CryptoData<>();
//
//        String egaprivatekey = cryptoConfig.getEgaPrivateKey(); // My private key kept private. this is base 64 encoded string
//
//        PrivateKey aPrivate = rsaService.getPrivate(egaprivatekey); // client singing
//        String jsonPlainData = rsaService.convertToJSON(sampleUserData);
//        String signatureString = rsaService.sign(jsonPlainData, aPrivate, cryptoConfig.getAlgorithm());
//
//        log.info("signatureString => " + signatureString);
//
//        cryptoData.setData(sampleUserData);
//        cryptoData.setSignature(signatureString);
//        return cryptoData;
//    }
//
////    public List<TaxCharges> createTaxCharges(BillItems billItem) {
////        List<TaxCharges> taxCharges = new ArrayList<>();
////        for (Tax tax : billItem.getService().getTaxes()) {
////            TaxCharges charges = new TaxCharges();
////            charges.setAccountCode(tax.getAccountCode());
////            charges.setTaxAmount(billItem.getTaxAmount());
////            charges.setTaxRate(tax.getRate());
////            charges.setDescription(tax.getDescription());
////
////            taxCharges.add(charges);
////        }
////        return taxCharges;
////    }
//
////    public List<Item> createBillItemList(Bill bill) {
////        List<Item> itemList = new ArrayList<>();
////
////        for (BillItems billItem : billItemRepository.findByBill(bill)) {
////            Item item = new Item();
////            item.setDescription(billItem.getDescription().replace("-", ""));
////            item.setQuantity(billItem.getQuantity());
////            item.setGfsCode(billItem.getGfsCode());
////            item.setAccountCode(billItem.getAccountCode());
////            item.setUnitPrice(billItem.getTotalAmount());
////            item.setTaxCharges(createTaxCharges(billItem));
////
////            itemList.add(item);
////        }
////
////        return itemList;
////    }
//
////    public ClientDto createClient(Long clientCode) {
////        InstitutionData data = uaaService.getInstitutionByClientCode(clientCode);
////        ClientDto client = new ClientDto();
////        client.setCode(clientCode.toString());
////        client.setClientType("PUBLIC_INSTITUTION");
////        client.setEmail(data.getEmail());
////        client.setName(data.getName());
////        client.setPhone(data.getTelephone1() != null && !data.getTelephone1().isEmpty() ? data.getTelephone1() : "255763292299");
////        return client;
////    }
//
//    public String verifySignature(@RequestBody String billingData) throws Exception {
//
//        processSignedData(billingData);
//
//        return billingData.toString();
//    }
//
//    @Override
//    public CryptoData<String> processSignedData(String receivedData) {
//        try {
//
//            log.info("received data: " + receivedData);
//            CryptoData<String> result = rsaService.convertJSONToObject(CryptoData.class, receivedData);
//
//            String plainData = rsaService.convertToJSON(result.getData());
//
//            log.info("plainData: " + plainData);
//
//            PublicKey aPublic = rsaService.getPublic(cryptoConfig.getclientPublicKey());
//
//            log.info("generatedPublicKey\n" + rsaService.getPEMString(aPublic));
//
//            String signature = result.getSignature();
//            log.info("signature: " + signature);
//
//            boolean verify = rsaService.verify(plainData, signature, aPublic, cryptoConfig.getAlgorithm());
//
//            log.info("Verified: {}", verify);
//            if (verify) {
//                log.info("VALID MESSAGE");
//
//            } else {
//                log.info("INVALID MESSAGE");
//            }
//
//            return result;
//        } catch (IOException ex) {
//            log.error("Exception: {}", ex);
//        } catch (Exception ex) {
//            log.error("Exception: {}", ex);
//        }
//        return null;
//    }
//
////    @Override
////    public List<Bill> listBills() {
////        PermissionsData permissionsData = uaaService.getPermissionByName("ROLE_SERVICE_REQUEST_VIEW_ALL_INSTITUTION_INVOICE");
////        if (loggedUser.hasPermission(Collections.singletonList(permissionsData.getId()))) {
////            return billRepository.findAll();
////        } else {
////            InstitutionData institutionData = uaaService.getInstitutionById(loggedUser.getInfo().getInstitutionId());
////            return billRepository.findByClientCode(institutionData.getClientCode().toString());
////        }
////    }
//
////    @Override
////    public List<Bill> listBillsByClient(String clientCode) {
////
////        return billRepository.findByClientCode(clientCode);
////    }
////
////    @Override
////    public Response<Bill> findBillByServiceRequestRefno(String serviceRequestRefNo) {
////        try {
////            Bill currentBill = billRepository.findByServiceRequestRefNo(serviceRequestRefNo);
////
////            return new Response<>(true, ResponseCode.SUCCESS, currentBill);
////        } catch (Exception e) {
////            log.error("Exception occured: {}", e);
////        }
////        return new Response<>(false, ResponseCode.FAILURE, null);
////    }
////
////    @Override
////    public Optional<Bill> getBillById(long billId) {
////
////        return billRepository.findById(billId);
////    }
////
////    @Override
////    public Bill updateBill(Bill bill) {
////
////        return billRepository.save(bill);
////    }
////
////    @Override
////    public Bill findByUid(String uid) {
////
////        return billRepository.findByUid(uid);
////    }
//
//
////    @Override
////    public Bill createBill(Billable billable) {
////
////        Bill bill = new Bill();
////        BigDecimal billAmount = BigDecimal.ZERO;
////        BigDecimal totalTaxAmount = BigDecimal.ZERO;
////
////        bill.setServiceRequestRefNo(billable.getReferenceNumber());
////        bill.setClientCode(billable.getClientCode().toString());
////        bill.setDescription(billable.getBillDescription().replaceAll("[^A-Za-z0-9]", " "));
////        bill.setBillable(billable);
////        bill.setModeOfSettlement(ModeOfSettlement.PARTIAL);
////        bill.setCreatedBy(billable.getCreatedBy());
////        bill.setUpdatedBy(billable.getUpdatedBy());
////        bill.setBillDate(LocalDateTime.now());
////        bill.setCnDelivered(false);
////        bill.setTaxAmount(totalTaxAmount);
////        bill.setTotalAmount(billAmount);
////        bill.setAmountDue(billAmount);
////        bill.setSourceRef("");
////        Bill savedBill = billRepository.save(bill);
////
////
////        for (BillItems billItem : billable.getBillItems()) {
////            log.info("Bill item tax amount: {}", totalTaxAmount);
////            totalTaxAmount = totalTaxAmount.add(billItem.getTaxAmount());
////            billAmount = billAmount.add(billItem.getTotalAmount());
////            billItem.setBill(savedBill);
////            billItemRepository.save(billItem);
////        }
////
////        BigDecimal totalBillAmount = billAmount.add(totalTaxAmount);
////        savedBill.setTaxAmount(totalTaxAmount);
////        savedBill.setTotalAmount(totalBillAmount);
////        savedBill.setAmountDue(totalBillAmount);
////
////        Bill updatedBill = billRepository.save(savedBill);
////
////        generateBill(updatedBill.getId());
////        return updatedBill;
////    }
//
////    @Override
////    public Bill findByBillNumber(String billNumber) {
////
////        return billRepository.findByBillNumber(billNumber);
////    }
//
////    @Override
////    public Bill cancelBillByBillNumber(String billNumber) {
////        Bill currBill = billRepository.findByBillNumber(billNumber);
////
////        if (currBill != null) {
////
////        }
////        return null;
////    }
//
//    public CryptoData<ERMSBillResponseData> generateCancelSignedData(ERMSBillResponseData sampleUserData) throws Exception {
//        CryptoData<ERMSBillResponseData> cryptoData = new CryptoData<>();
//
//        String egaprivatekey = cryptoConfig.getEgaPrivateKey(); // My private key kept private. this is base 64 encoded string
//
//        PrivateKey aPrivate = rsaService.getPrivate(egaprivatekey); // client singing
//        String jsonPlainData = rsaService.convertToJSON(sampleUserData);
//        String signatureString = rsaService.sign(jsonPlainData, aPrivate, cryptoConfig.getAlgorithm());
//
//        log.info("signatureString => " + signatureString);
//
//        cryptoData.setData(sampleUserData);
//        cryptoData.setSignature(signatureString);
//
////        GispHelper.print(cryptoData);
//        return cryptoData;
//    }
//
////    @Override
////    public DataTablesOutput<Bill> listBillsDt(Long instId, DataTablesInput dataTablesInput) {
////        return billDtRepository.findAll(dataTablesInput, billSpecification(instId));
////    }
//
//
////    private Specification<Bill> billSpecification(Long instId) {
////        PermissionsData permissionsData = uaaService.getPermissionByName("ROLE_SERVICE_REQUEST_VIEW_ALL_INSTITUTION_INVOICE");
////
////        return (root, query, criteriaBuilder) -> {
////            List<Predicate> predicates = new ArrayList<>();
////            if (!loggedUser.hasPermission(Collections.singletonList(permissionsData.getId()))) {
////                InstitutionData institutionData = uaaService.getInstitutionById(loggedUser.getInfo().getInstitutionId());
////                log.info("Fetching by client code because no permission to view all: client {}", institutionData);
////                predicates.add(criteriaBuilder.equal(root.get("clientCode"), institutionData.getClientCode().toString()));
////            }
////
////            if (instId != null) {
////                InstitutionData institutionData = uaaService.getInstitutionById(instId);
////                log.info("Fetching by passed institution id: client {}", institutionData);
////                predicates.add(criteriaBuilder.equal(root.get("clientCode"), institutionData.getClientCode().toString()));
////            }
////            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
////        };
////
////    }
//}
