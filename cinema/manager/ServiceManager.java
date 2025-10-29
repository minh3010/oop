package cinema.manager;

import cinema.model.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServiceManager {
    private List<Service> services;

    public ServiceManager() {
        this.services = new ArrayList<>();
    }

    public void addService(Service service) {
        services.add(service);
    }

    public boolean removeService(String serviceId) {
        return services.removeIf(s -> s.getId().equals(serviceId));
    }

    public Optional<Service> findServiceById(String id) {
        return services.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst();
    }

    public List<Service> getAllServices() {
        return new ArrayList<>(services);
    }

    public List<Service> getAvailableServices() {
        List<Service> result = new ArrayList<>();
        for (Service service : services) {
            if (service.isAvailable()) {
                result.add(service);
            }
        }
        return result;
    }

    public List<Service> getServicesByType(String type) {
        List<Service> result = new ArrayList<>();
        for (Service service : services) {
            if (service.getType().equalsIgnoreCase(type)) {
                result.add(service);
            }
        }
        return result;
    }

    public void displayAllServices() {
        if (services.isEmpty()) {
            System.out.println("Không có dịch vụ nào trong hệ thống.");
            return;
        }
        System.out.println("\n=== DANH SÁCH DỊCH VỤ ===");
        for (Service service : services) {
            System.out.println(service);
        }
    }

    public void displayAvailableServices() {
        List<Service> available = getAvailableServices();
        if (available.isEmpty()) {
            System.out.println("Không có dịch vụ nào còn hàng.");
            return;
        }
        System.out.println("\n=== DỊCH VỤ CÒN HÀNG ===");
        for (Service service : available) {
            System.out.println(service);
        }
    }
}
