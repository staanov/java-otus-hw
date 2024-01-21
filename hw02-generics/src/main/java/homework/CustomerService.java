package homework;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    // todo: 3. надо реализовать методы этого класса
    // важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны
    TreeMap<Customer, String> customerData = new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        // Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk
        return copy(customerData.firstEntry());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        long scores = customer.getScores();
        for (Map.Entry<Customer, String> entry : customerData.entrySet()) {
            if (entry.getKey().getScores() > scores) {
                return copy(entry);
            }
        }
        return null;
    }

    public void add(Customer customer, String data) {
        customerData.put(customer, data);
    }

    private Map.Entry<Customer, String> copy(Map.Entry<Customer, String> entry) {
        return Map.entry(new Customer(entry.getKey().getId(), entry.getKey().getName(), entry.getKey().getScores()),
            entry.getValue());
    }
}
