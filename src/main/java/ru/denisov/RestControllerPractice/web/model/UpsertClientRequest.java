package ru.denisov.RestControllerPractice.web.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpsertClientRequest {

    @NotBlank(message = "Имя клиента должно быть заполнено!")
    @Size(min = 3, max = 30, message = "Имя клиента не может быть меньше {min} и больше {max}!")
    private String name;

}
