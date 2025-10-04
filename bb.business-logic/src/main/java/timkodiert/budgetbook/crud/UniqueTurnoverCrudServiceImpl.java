package timkodiert.budgetbook.crud;

import java.util.List;
import java.util.Objects;

import jakarta.inject.Inject;
import org.jetbrains.annotations.Nullable;
import org.mapstruct.factory.Mappers;

import timkodiert.budgetbook.domain.FixedTurnoverDTO;
import timkodiert.budgetbook.domain.Reference;
import timkodiert.budgetbook.domain.UniqueTurnoverCrudService;
import timkodiert.budgetbook.domain.UniqueTurnoverDTO;
import timkodiert.budgetbook.domain.UniqueTurnoverInformationDTO;
import timkodiert.budgetbook.domain.model.BaseEntity;
import timkodiert.budgetbook.domain.model.UniqueTurnover;
import timkodiert.budgetbook.domain.model.UniqueTurnoverInformation;
import timkodiert.budgetbook.domain.repository.UniqueExpenseInformationRepository;
import timkodiert.budgetbook.domain.repository.UniqueExpensesRepository;

import static timkodiert.budgetbook.util.ObjectUtils.nvl;

public class UniqueTurnoverCrudServiceImpl implements UniqueTurnoverCrudService {

    private final ReferenceResolver referenceResolver;
    private final UniqueExpensesRepository uniqueTurnoverRepository;
    private final UniqueExpenseInformationRepository uniqueTurnoverInformationRepository;

    @Inject
    public UniqueTurnoverCrudServiceImpl(ReferenceResolver referenceResolver,
                                         UniqueExpensesRepository uniqueTurnoverRepository,
                                         UniqueExpenseInformationRepository uniqueTurnoverInformationRepository) {
        this.referenceResolver = referenceResolver;
        this.uniqueTurnoverRepository = uniqueTurnoverRepository;
        this.uniqueTurnoverInformationRepository = uniqueTurnoverInformationRepository;
    }

    @Override
    public List<UniqueTurnoverDTO> readAll(@Nullable Reference<FixedTurnoverDTO> fixedTurnoverRef) {
        UniqueTurnoverMapper mapper = Mappers.getMapper(UniqueTurnoverMapper.class);
        return uniqueTurnoverRepository.findAll()
                                       .stream()
                                       .filter(uq -> Objects.equals(nvl(uq.getFixedTurnover(), BaseEntity::getId), nvl(fixedTurnoverRef, Reference::id)))
                                       .map(mapper::uniqueTurnoverToUniqueTurnoverDto)
                                       .toList();
    }

    @Override
    public UniqueTurnoverDTO readById(int id) {
        UniqueTurnoverMapper mapper = Mappers.getMapper(UniqueTurnoverMapper.class);
        return mapper.uniqueTurnoverToUniqueTurnoverDto(uniqueTurnoverRepository.findById(id));
    }

    @Override
    public UniqueTurnoverInformationDTO readUniqueTurnoverInformationById(int id) {
        UniqueTurnoverMapper mapper = Mappers.getMapper(UniqueTurnoverMapper.class);
        return mapper.uniqueTurnoverInformationToUniqueTurnoverInformationDto(uniqueTurnoverInformationRepository.findById(id));
    }

    @Override
    public boolean create(UniqueTurnoverDTO uniqueTurnoverDTO) {
        UniqueTurnover uniqueTurnover = new UniqueTurnover();
        UniqueTurnoverMapper uniqueTurnoverMapper = Mappers.getMapper(UniqueTurnoverMapper.class);
        uniqueTurnoverMapper.updateUniqueTurnover(uniqueTurnoverDTO, uniqueTurnover, referenceResolver);
        linkTurnoverInformation(uniqueTurnover);
        uniqueTurnoverRepository.merge(uniqueTurnover);
        return true;
    }

    @Override
    public boolean update(UniqueTurnoverDTO uniqueTurnoverDTO) {
        UniqueTurnover uniqueTurnover = uniqueTurnoverRepository.findById(uniqueTurnoverDTO.getId());
        UniqueTurnoverMapper uniqueTurnoverMapper = Mappers.getMapper(UniqueTurnoverMapper.class);
        uniqueTurnoverMapper.updateUniqueTurnover(uniqueTurnoverDTO, uniqueTurnover, referenceResolver);
        linkTurnoverInformation(uniqueTurnover);
        uniqueTurnoverRepository.merge(uniqueTurnover);
        return true;
    }

    @Override
    public boolean delete(int id) {
        UniqueTurnover uniqueTurnover = uniqueTurnoverRepository.findById(id);
        if (uniqueTurnover == null) {
            return false;
        }
        uniqueTurnoverRepository.remove(uniqueTurnover);
        return true;
    }

    private void linkTurnoverInformation(UniqueTurnover uniqueTurnover) {
        uniqueTurnover.getPaymentInformations().forEach(info -> {
            info.setExpense(uniqueTurnover);
            if (info.getId() < 0) {
                info.setId(0);
            }
        });
    }

    @Override
    public List<String> getUniqueTurnoverLabels() {
        return uniqueTurnoverRepository.findAll().stream().map(UniqueTurnover::getBiller).distinct().toList();
    }

    @Override
    public List<String> getUniqueTurnoverInformationLabels() {
        return uniqueTurnoverInformationRepository.findAll().stream().map(UniqueTurnoverInformation::getLabel).distinct().toList();
    }
}
