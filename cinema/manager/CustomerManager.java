package cinema.manager;

import cinema.model.Customer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerManager {
    private List<Customer> customers;

    public CustomerManager() {
        this.customers = new ArrayList<>();
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public boolean removeCustomer(String customerId) {
        return customers.removeIf(c -> c.getId().equals(customerId));
    }

    public Optional<Customer> findCustomerById(String id) {
        return customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    public Optional<Customer> findCustomerByPhone(String phone) {
        return customers.stream()
                .filter(c -> c.getPhone().equals(phone))
                .findFirst();
    }

    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers);
    }

    public List<Customer> searchByName(String name) {
        List<Customer> result = new ArrayList<>();
        for (Customer customer : customers) {
            if (customer.getName().toLowerCase().contains(name.toLowerCase())) {
                result.add(customer);
            }
        }
        return result;
    }

    public List<Customer> getTopCustomers(int limit) {
        List<Customer> sorted = new ArrayList<>(customers);
        sorted.sort((c1, c2) -> Integer.compare(c2.getLoyaltyPoints(), c1.getLoyaltyPoints()));
        return sorted.subList(0, Math.min(limit, sorted.size()));
    }

    public void displayAllCustomers() {
        if (customers.isEmpty()) {
            System.out.println("Không có khách hàng nào trong hệ thống.");
            return;
        }
        System.out.println("\n=== DANH SÁCH KHÁCH HÀNG ===");
        for (Customer customer : customers) {
            System.out.println(customer);
        }
    }
}
