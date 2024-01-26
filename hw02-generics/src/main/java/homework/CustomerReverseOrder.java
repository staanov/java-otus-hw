package homework;

import java.util.ArrayDeque;

public class CustomerReverseOrder {

    // todo: 2. надо реализовать методы этого класса
    // надо подобрать подходящую структуру данных, тогда решение будет в "две строчки"
    ArrayDeque<Customer> deque = new ArrayDeque<>();

    public void add(Customer customer) {
        deque.add(customer);
    }

    public Customer take() {
        return deque.pollLast();
    }
}
