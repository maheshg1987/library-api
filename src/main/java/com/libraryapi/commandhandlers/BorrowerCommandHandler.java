package com.libraryapi.commandhandlers;


import an.awesome.pipelinr.Command;
import com.libraryapi.commands.BorrowerCommand;
import com.libraryapi.dao.repositories.BorrowerRepository;
import com.libraryapi.dtos.BorrowerResponseDTO;
import com.libraryapi.mappers.CommonMapper;
import com.libraryapi.model.Borrower;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class BorrowerCommandHandler implements Command.Handler<BorrowerCommand, BorrowerResponseDTO> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final BorrowerRepository borrowerRepository;
    private final CommonMapper mapper;

    public BorrowerCommandHandler(BorrowerRepository borrowerRepository, CommonMapper mapper) {
        this.borrowerRepository = borrowerRepository;
        this.mapper = mapper;
    }


    @Override
    public BorrowerResponseDTO handle(BorrowerCommand command) {
        logger.debug("Attempt to register a borrower for name: {}", command.getName());
        Borrower savedBorrower = borrowerRepository.saveAndFlush(createBorrower(command));
        logger.debug("Borrower saved successfully with the id: {}", savedBorrower.getId());
        return mapper.toBorrowerResponseDTO(savedBorrower);
    }


    private Borrower createBorrower(BorrowerCommand command) {
        return new Borrower(command.getName(), command.getEmail());
    }

}
