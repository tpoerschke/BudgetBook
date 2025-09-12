package timkodiert.budgetbook.crud;

import java.util.List;

import jakarta.inject.Inject;
import org.mapstruct.factory.Mappers;

import timkodiert.budgetbook.domain.FixedTurnoverCrudService;
import timkodiert.budgetbook.domain.FixedTurnoverDTO;
import timkodiert.budgetbook.domain.PaymentInformationDTO;
import timkodiert.budgetbook.domain.model.FixedTurnover;
import timkodiert.budgetbook.domain.model.PaymentInformation;
import timkodiert.budgetbook.domain.repository.Repository;

public class FixedTurnoverCrudServiceImpl implements FixedTurnoverCrudService {

    private final ReferenceResolver referenceResolver;
    private final Repository<FixedTurnover> fixedTurnoverRepository;
    private final Repository<PaymentInformation> paymentInformationRepository;

    @Inject
    public FixedTurnoverCrudServiceImpl(ReferenceResolver referenceResolver,
                                        Repository<FixedTurnover> fixedTurnoverRepository,
                                        Repository<PaymentInformation> paymentInformationRepository) {
        this.referenceResolver = referenceResolver;
        this.fixedTurnoverRepository = fixedTurnoverRepository;
        this.paymentInformationRepository = paymentInformationRepository;
    }

    @Override
    public List<FixedTurnoverDTO> readAll() {
        FixedTurnoverMapper mapper = Mappers.getMapper(FixedTurnoverMapper.class);
        return fixedTurnoverRepository.findAll().stream().map(mapper::fixedTurnoverToFixedTurnoverDto).toList();
    }

    @Override
    public FixedTurnoverDTO readById(int id) {
        FixedTurnoverMapper mapper = Mappers.getMapper(FixedTurnoverMapper.class);
        return fixedTurnoverRepository.findAll().stream().filter(t -> t.getId() == id).findAny().map(mapper::fixedTurnoverToFixedTurnoverDto).orElse(null);
    }

    @Override
    public PaymentInformationDTO readPaymentInformationById(int id) {
        FixedTurnoverMapper mapper = Mappers.getMapper(FixedTurnoverMapper.class);
        PaymentInformation payInfo = paymentInformationRepository.findById(id);
        return mapper.paymentInformationToPaymentInformationDto(payInfo);
    }

    @Override
    public boolean create(FixedTurnoverDTO fixedTurnoverDTO) {
        FixedTurnover fixedTurnover = new FixedTurnover();
        FixedTurnoverMapper fixedTurnoverMapper = Mappers.getMapper(FixedTurnoverMapper.class);
        fixedTurnoverMapper.updateFixedTurnover(fixedTurnoverDTO, fixedTurnover, referenceResolver);
        linkSubEntities(fixedTurnover);
        fixedTurnoverRepository.merge(fixedTurnover);
        return true;
    }

    @Override
    public boolean update(FixedTurnoverDTO fixedTurnoverDTO) {
        FixedTurnover fixedTurnover = fixedTurnoverRepository.findById(fixedTurnoverDTO.getId());
        FixedTurnoverMapper fixedTurnoverMapper = Mappers.getMapper(FixedTurnoverMapper.class);
        fixedTurnoverMapper.updateFixedTurnover(fixedTurnoverDTO, fixedTurnover, referenceResolver);
        linkSubEntities(fixedTurnover);
        fixedTurnoverRepository.merge(fixedTurnover);
        return true;
    }

    @Override
    public boolean delete(int id) {
        FixedTurnover fixedTurnover = fixedTurnoverRepository.findById(id);
        if (fixedTurnover == null) {
            return false;
        }
        fixedTurnoverRepository.remove(fixedTurnover);
        return true;
    }

    private void linkSubEntities(FixedTurnover fixedTurnover) {
        fixedTurnover.getPaymentInformations().forEach(payInfo -> {
            payInfo.setExpense(fixedTurnover);
            if (payInfo.getId() < 0) {
                payInfo.setId(0);
            }
        });
        fixedTurnover.getImportRules().forEach(importRule -> {
            if (importRule.getId() < 0) {
                importRule.setId(0);
            }
            importRule.setLinkedFixedExpense(fixedTurnover);
        });
    }
}
