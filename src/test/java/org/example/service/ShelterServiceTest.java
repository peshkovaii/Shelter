package org.example.service;

import org.example.core.Adopter;
import org.example.core.AdoptionRequest;
import org.example.core.Animal;
import org.example.core.enums.AnimalStatus;
import org.example.core.enums.RequestStatus;
import org.example.core.enums.Species;
import org.example.repository.AdopterRepository;
import org.example.repository.AdoptionRequestRepository;
import org.example.repository.AnimalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShelterServiceTest {

    private AnimalRepository animalRepository;
    private AdopterRepository adopterRepository;
    private AdoptionRequestRepository requestRepository;
    private ShelterService service;

    @BeforeEach
    void setUp() {
        animalRepository = new InMemoryAnimalRepository();
        adopterRepository = new InMemoryAdopterRepository();
        requestRepository = new InMemoryAdoptionRequestRepository();
        service = new ShelterService(animalRepository, adopterRepository, requestRepository);
    }

    @Test
    void addsAnimalAndAssignsId() {
        Animal animal = new Animal("Барсик", Species.CAT, "Британская", 3);
        Animal saved = service.addAnimal(animal);
        assertEquals(1L, saved.getId());
        assertEquals(1, service.getAllAnimals().size());
        assertEquals(AnimalStatus.IN_SHELTER, saved.getStatus());
    }

    @Test
    void invalidAnimalIsNotSaved() {
        Animal animal = new Animal("   ", Species.CAT, "Британская", 3);
        assertThrows(IllegalArgumentException.class, () -> service.addAnimal(animal));
        assertTrue(service.getAllAnimals().isEmpty());
    }

    @Test
    void getAnimalReturnsSavedAnimal() {
        Animal saved = service.addAnimal(new Animal("Барсик", Species.CAT, "Британская", 3));
        Animal found = service.getAnimal(saved.getId());
        assertEquals("Барсик", found.getName());
    }

    @Test
    void getMissingAnimalThrows() {
        assertThrows(IllegalArgumentException.class, () -> service.getAnimal(999L));
    }

    @Test
    void deletesAnimalWithoutRequests() {
        Animal saved = service.addAnimal(new Animal("Барсик", Species.CAT, "Британская", 3));
        service.deleteAnimal(saved.getId());
        assertTrue(service.getAllAnimals().isEmpty());
    }

    @Test
    void cannotDeleteMissingAnimal() {
        assertThrows(IllegalArgumentException.class, () -> service.deleteAnimal(999L));
    }

    @Test
    void cannotDeleteAnimalWithRequests() {
        Animal animal = service.addAnimal(new Animal("Барсик", Species.CAT, "Британская", 3));
        Adopter adopter = service.addAdopter(new Adopter("Иванов Иван", "+79991234567", "ivan@mail.ru"));
        service.createRequest(animal.getId(), adopter.getId());
        assertThrows(IllegalStateException.class, () -> service.deleteAnimal(animal.getId()));
        assertTrue(animalRepository.findById(animal.getId()).isPresent());
    }

    @Test
    void addsAdopterAndAssignsId() {
        Adopter adopter = new Adopter("Иванов Иван", "+79991234567", "ivan@mail.ru");
        Adopter saved = service.addAdopter(adopter);
        assertEquals(1L, saved.getId());
        assertEquals(1, service.getAllAdopters().size());
    }

    @Test
    void invalidAdopterIsNotSaved() {
        Adopter adopter = new Adopter("Иванов Иван", "+79991234567", "ivanmail.ru");
        assertThrows(IllegalArgumentException.class, () -> service.addAdopter(adopter));
        assertTrue(service.getAllAdopters().isEmpty());
    }

    @Test
    void createsRequestWithPendingStatus() {
        Animal animal = service.addAnimal(new Animal("Барсик", Species.CAT, "Британская", 3));
        Adopter adopter = service.addAdopter(new Adopter("Иванов Иван", "+79991234567", "ivan@mail.ru"));
        AdoptionRequest request = service.createRequest(animal.getId(), adopter.getId());
        assertEquals(RequestStatus.PENDING, request.getStatus());
        assertEquals(animal.getId(), request.getAnimalId());
        assertEquals(adopter.getId(), request.getAdopterId());
        assertEquals(1, service.getAllRequests().size());
    }

    @Test
    void cannotCreateRequestForMissingAnimal() {
        Adopter adopter = service.addAdopter(new Adopter("Иванов Иван", "+79991234567", "ivan@mail.ru"));
        assertThrows(IllegalArgumentException.class, () -> service.createRequest(999L, adopter.getId()));
        assertTrue(service.getAllRequests().isEmpty());
    }

    @Test
    void cannotCreateRequestForMissingAdopter() {
        Animal animal = service.addAnimal(new Animal("Барсик", Species.CAT, "Британская", 3));
        assertThrows(IllegalArgumentException.class, () -> service.createRequest(animal.getId(), 999L));
        assertTrue(service.getAllRequests().isEmpty());
    }

    @Test
    void cannotCreateRequestForAlreadyAdoptedAnimal() {
        Animal animal = service.addAnimal(new Animal("Барсик", Species.CAT, "Британская", 3));
        Adopter adopter = service.addAdopter(new Adopter("Иванов Иван", "+79991234567", "ivan@mail.ru"));
        AdoptionRequest first = service.createRequest(animal.getId(), adopter.getId());
        service.approveRequest(first.getId());
        assertThrows(IllegalStateException.class, () -> service.createRequest(animal.getId(), adopter.getId()));
    }

    @Test
    void approveRequestMarksRequestApprovedAndAnimalAdopted() {
        Animal animal = service.addAnimal(new Animal("Барсик", Species.CAT, "Британская", 3));
        Adopter adopter = service.addAdopter(new Adopter("Иванов Иван", "+79991234567", "ivan@mail.ru"));
        AdoptionRequest request = service.createRequest(animal.getId(), adopter.getId());
        service.approveRequest(request.getId());
        assertEquals(RequestStatus.APPROVED, request.getStatus());
        assertEquals(AnimalStatus.ADOPTED, service.getAnimal(animal.getId()).getStatus());
    }

    @Test
    void approveRequestRejectsOtherPendingRequestsForSameAnimal() {
        Animal animal = service.addAnimal(new Animal("Барсик", Species.CAT, "Британская", 3));
        Adopter first = service.addAdopter(new Adopter("Иванов Иван", "+79991234567", "ivan@mail.ru"));
        Adopter second = service.addAdopter(new Adopter("Петров Пётр", "+79997654321", "petr@mail.ru"));
        AdoptionRequest approved = service.createRequest(animal.getId(), first.getId());
        AdoptionRequest other = service.createRequest(animal.getId(), second.getId());
        service.approveRequest(approved.getId());
        assertEquals(RequestStatus.APPROVED, approved.getStatus());
        assertEquals(RequestStatus.REJECTED, other.getStatus());
    }

    @Test
    void cannotApproveMissingRequest() {
        assertThrows(IllegalArgumentException.class, () -> service.approveRequest(999L));
    }

    @Test
    void cannotApproveAlreadyProcessedRequest() {
        Animal animal = service.addAnimal(new Animal("Барсик", Species.CAT, "Британская", 3));
        Adopter adopter = service.addAdopter(new Adopter("Иванов Иван", "+79991234567", "ivan@mail.ru"));
        AdoptionRequest request = service.createRequest(animal.getId(), adopter.getId());
        service.approveRequest(request.getId());
        assertThrows(IllegalStateException.class, () -> service.approveRequest(request.getId()));
    }

    @Test
    void cannotApproveRequestWhenAnimalAlreadyAdopted() {
        Animal animal = service.addAnimal(new Animal("Барсик", Species.CAT, "Британская", 3));
        Adopter first = service.addAdopter(new Adopter("Иванов Иван", "+79991234567", "ivan@mail.ru"));
        Adopter second = service.addAdopter(new Adopter("Петров Пётр", "+79997654321", "petr@mail.ru"));
        AdoptionRequest approved = service.createRequest(animal.getId(), first.getId());
        AdoptionRequest stale = service.createRequest(animal.getId(), second.getId());
        service.approveRequest(approved.getId());
        assertThrows(IllegalStateException.class, () -> service.approveRequest(stale.getId()));
    }

    static class InMemoryAnimalRepository implements AnimalRepository {
        private final Map<Long, Animal> store = new HashMap<>();
        private long nextId = 1;

        @Override
        public Animal save(Animal animal) {
            if (animal.getId() == null) {
                animal.setId(nextId++);
            }
            store.put(animal.getId(), animal);
            return animal;
        }

        @Override
        public Optional<Animal> findById(Long id) {
            return Optional.ofNullable(store.get(id));
        }

        @Override
        public List<Animal> findAll() {
            return new ArrayList<>(store.values());
        }

        @Override
        public void deleteById(Long id) {
            store.remove(id);
        }
    }

    static class InMemoryAdopterRepository implements AdopterRepository {
        private final Map<Long, Adopter> store = new HashMap<>();
        private long nextId = 1;

        @Override
        public Adopter save(Adopter adopter) {
            if (adopter.getId() == null) {
                adopter.setId(nextId++);
            }
            store.put(adopter.getId(), adopter);
            return adopter;
        }

        @Override
        public Optional<Adopter> findById(Long id) {
            return Optional.ofNullable(store.get(id));
        }

        @Override
        public List<Adopter> findAll() {
            return new ArrayList<>(store.values());
        }

        @Override
        public void deleteById(Long id) {
            store.remove(id);
        }
    }

    static class InMemoryAdoptionRequestRepository implements AdoptionRequestRepository {
        private final Map<Long, AdoptionRequest> store = new HashMap<>();
        private long nextId = 1;

        @Override
        public AdoptionRequest save(AdoptionRequest request) {
            if (request.getId() == null) {
                request.setId(nextId++);
            }
            store.put(request.getId(), request);
            return request;
        }

        @Override
        public Optional<AdoptionRequest> findById(Long id) {
            return Optional.ofNullable(store.get(id));
        }

        @Override
        public List<AdoptionRequest> findAll() {
            return new ArrayList<>(store.values());
        }

        @Override
        public void deleteById(Long id) {
            store.remove(id);
        }
    }
}