package ru.denisov.RestControllerPractice.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientListResponse {

    private List<ClientResponse> clients = new ArrayList<>();
}
