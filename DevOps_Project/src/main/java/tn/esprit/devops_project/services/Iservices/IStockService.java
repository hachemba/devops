package tn.esprit.devops_project.services.Iservices;

import tn.esprit.devops_project.entities.Stock;

import java.util.List;

public interface IStockService {

    Stock addStock(Stock stock);
    Stock retrieveStock(Long id);
    List<Stock> retrieveAllStock();

    Stock updateStock(Stock stock);
    void deleteStock(Long id);
    List<Stock> findStocksByTitle(String title);

}
