package com.example.simplebankingapp.impl;

import com.example.simplebankingapp.dto.AccountDto;
import com.example.simplebankingapp.entity.Account;
import com.example.simplebankingapp.mapper.AccountMapper;
import com.example.simplebankingapp.repository.AccountRepository;
import com.example.simplebankingapp.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    private AccountRepository accountRepository;

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Account account = AccountMapper.mapToAccount(accountDto);
        Account savedAccount = accountRepository.save(account);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto getAccountById(Long id) {
       Account account =  accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
       return AccountMapper.mapToAccountDto(account);
    }

    @Override
    public AccountDto deposit(Long id, Double amount) {
        Account account =  accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
        double totalAmount = account.getBalance()+amount;
        account.setBalance(totalAmount);
        Account savedAccount = accountRepository.save(account);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto withdraw(Long id, Double amount) {
        Account account =  accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));

        if(account.getBalance()<amount){
            throw new RuntimeException("Insufficient balance");
        }
        double totalAmount = account.getBalance()-amount;
        account.setBalance(totalAmount);
        Account savedAccount = accountRepository.save(account);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public List<AccountDto> getAllAccounts() {
       List<Account> accounts = accountRepository.findAll();
      return accounts.stream().map((account) -> AccountMapper.mapToAccountDto(account))
              .collect(Collectors.toList());
    }
}
