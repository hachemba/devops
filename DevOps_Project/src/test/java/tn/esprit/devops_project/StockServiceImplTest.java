package tn.esprit.devops_project;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.devops_project.entities.Stock;
import tn.esprit.devops_project.repositories.StockRepository;
import tn.esprit.devops_project.services.StockServiceImpl;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
class StockServiceImplTest {
    @InjectMocks
    private StockServiceImpl stockService;

    @Autowired
    private StockServiceImpl stockServiceAuto;

    @Autowired
    private StockRepository stockRepositoryAuto;

    @Mock
    private StockRepository stockRepository;


    private void assertStocksEqual(Stock stock1, Stock stock2) {
        assertEquals(stock1.getIdStock(), stock2.getIdStock());
        assertEquals(stock1.getTitle(), stock2.getTitle());
    }

    @BeforeEach
    void setUp() {
        List<Stock> mockStocks = Arrays.asList(
                new Stock(1L, "Stock 1"),
                new Stock(2L, "Stock 2"),
                new Stock(3L, "Stock 3") // Add a stock item with the title "Stock 3"
        );
        Mockito.when(stockRepository.findByTitle(any(String.class)))
                .thenAnswer(invocation -> {
                    String title = invocation.getArgument(0);
                    return mockStocks.stream()
                            .filter(stock -> stock.getTitle().equals(title))
                            .collect(Collectors.toList());
                });
    }


    @Test
    public void testAddStock() {
        Stock mockStock = new Stock();
        mockStock.setIdStock(1L);
        mockStock.setTitle("Test Stock");
        mockStock.setProducts(new HashSet<>());

        when(stockRepository.save(mockStock)).thenReturn(mockStock);

          Stock addedStock = stockService.addStock(mockStock);
          //Stock addedStock = stockService.addStock(new Stock());

        verify(stockRepository).save(mockStock);

        //assertStocksEqual(addedStock, mockStock);
        assertNotNull(addedStock);
        assertEquals("Test Stock", addedStock.getTitle());
        assertEquals(addedStock.getIdStock(), mockStock.getIdStock());
    }


    @Test
    public void testRetrieveStockById() {
        Long stockId = 1L;
        Stock mockStock = new Stock();
        mockStock.setIdStock(stockId);
        Mockito.when(stockRepository.findById(stockId)).thenReturn(Optional.of(mockStock));

        Stock retrievedStock = stockService.retrieveStock(stockId);
        verify(stockRepository, times(1)).findById(1L);
        assertSame(mockStock, retrievedStock);
        Assertions.assertThat(retrievedStock.getIdStock()).isEqualTo(stockId);


        Long invalidStockId = 99L;
        Mockito.when(stockRepository.findById(invalidStockId)).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> stockService.retrieveStock(invalidStockId));

    }

    @Test
    public void testRetrieveAllStock() {
        List<Stock> mockStocks = new ArrayList<>();
        mockStocks.add(new Stock());
        mockStocks.add(new Stock());

        Mockito.when(stockRepository.findAll()).thenReturn(mockStocks);

        List<Stock> retrievedStocks = stockService.retrieveAllStock();
        List<Stock> stocks = stockService.retrieveAllStock();
        verify(stockRepository, times(2)).findAll();

        Assertions.assertThat(retrievedStocks).hasSize(2);
    }

    @Test
    public void testFindStocksByTitle() {

        List<Stock> foundStocks = stockService.findStocksByTitle("Stock 1");
        Assertions.assertThat(foundStocks).hasSize(1);
        Assertions.assertThat(foundStocks.get(0).getTitle()).isEqualTo("Stock 1");

        List<Stock> notFoundStocks = stockService.findStocksByTitle("Stock 4");
        Assertions.assertThat(notFoundStocks).isEmpty();
    }

    @Test
    public void testDeleteStock() {
        Long stockId = 1L;
        doNothing().when(stockRepository).deleteById(stockId);

        stockService.deleteStock(stockId);
        System.out.println(stockRepository.findAll());
        verify(stockRepository, times(1)).deleteById(stockId);
    }

    @Test
    public void testUpdateStock() {
        Stock mockStock = new Stock();
        mockStock.setIdStock(1L);
        mockStock.setTitle("Test Stock");
        mockStock.setProducts(new HashSet<>());

        when(stockRepository.save(mockStock)).thenReturn(mockStock);

        Stock updatedStock = stockService.updateStock(mockStock);

        verify(stockRepository).save(mockStock);

        assertNotNull(updatedStock);
        assertEquals("Test Stock", updatedStock.getTitle());
        assertEquals(updatedStock.getIdStock(), mockStock.getIdStock());
    }

}