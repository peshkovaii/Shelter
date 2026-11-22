package org.example.service;



import org.example.core.Adopter;
import org.example.core.AdoptionRequest;
import org.example.core.Animal;
import org.example.core.enums.AnimalStatus;
import org.example.core.enums.RequestStatus;
import org.example.repository.AdopterRepository;
import org.example.repository.AdoptionRequestRepository;
import org.example.repository.AnimalRepository;
import org.example.validator.AnimalValidator;
import org.example.validator.AdopterValidator;

import java.util.List;

public class ShelterService {
    private final AnimalRepository animalRepository;
    private final AdopterRepository adopterRepository;
    private final AdoptionRequestRepository requestRepository;
    private final AnimalValidator animalValidator = new AnimalValidator();
    private final AdopterValidator adopterValidator = new AdopterValidator();

    public ShelterService(AnimalRepository animalRepository, AdopterRepository adopterRepository,AdoptionRequestRepository requestRepository){
        this.animalRepository = animalRepository;
        this.adopterRepository = adopterRepository;
        this.requestRepository = requestRepository;
    }

    public Animal addAnimal(Animal animal){
        animalValidator.validate(animal);
        return animalRepository.save(animal);
    }

    public List<Animal> getAllAnimals(){
        return animalRepository.findAll();
    }

    public Animal getAnimal(Long id){
        return animalRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Животное не найдено: " + id));
    }

    public void deleteAnimal(Long id) {
        getAnimal(id);

        for (AdoptionRequest request : requestRepository.findAll()) {
            if (id.equals(request.getAnimalId())) {
                throw new IllegalStateException(
                        "Нельзя удалить животное, на которое есть заявки"
                );
            }
        }

        animalRepository.deleteById(id);
    }

    public Adopter addAdopter(Adopter adopter){
        adopterValidator.validate(adopter);
        return adopterRepository.save(adopter);
    }

    public List<Adopter> getAllAdopters(){
        return adopterRepository.findAll();
    }



    public AdoptionRequest createRequest(Long animalId, Long adopterId) {
        Animal animal = getAnimal(animalId);

        if (animal.getStatus() != AnimalStatus.IN_SHELTER) {
            throw new IllegalStateException(
                    "Животное сейчас недоступно для усыновления"
            );
        }

        adopterRepository.findById(adopterId).orElseThrow(
                () -> new IllegalArgumentException("Усыновитель не найден")
        );

        AdoptionRequest request = new AdoptionRequest(animalId, adopterId);
        return requestRepository.save(request);
    }


    public List<AdoptionRequest> getAllRequests(){
        return requestRepository.findAll();
    }



    public void approveRequest(Long requestId) {
        AdoptionRequest request = requestRepository.findById(requestId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Заявка не найдена")
                );

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new IllegalStateException("Заявка уже рассмотрена");
        }

        Animal animal = getAnimal(request.getAnimalId());

        if (animal.getStatus() != AnimalStatus.IN_SHELTER) {
            throw new IllegalStateException(
                    "Животное сейчас недоступно для усыновления"
            );
        }

        request.setStatus(RequestStatus.APPROVED);
        animal.setStatus(AnimalStatus.ADOPTED);

        animalRepository.save(animal);
        requestRepository.save(request);

        for (AdoptionRequest other : requestRepository.findAll()) {
            if (!other.getId().equals(requestId)
                    && other.getAnimalId().equals(animal.getId())
                    && other.getStatus() == RequestStatus.PENDING) {
                other.setStatus(RequestStatus.REJECTED);
                requestRepository.save(other);
            }
        }
    }


}
