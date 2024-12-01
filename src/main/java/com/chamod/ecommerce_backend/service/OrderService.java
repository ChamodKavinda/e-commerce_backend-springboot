package com.chamod.ecommerce_backend.service;

import com.chamod.ecommerce_backend.model.LocalUser;
import com.chamod.ecommerce_backend.model.WebOrder;
import com.chamod.ecommerce_backend.model.dao.WebOrderDAO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private WebOrderDAO webOrderDAO;

    public OrderService(WebOrderDAO webOrderDAO) {
        this.webOrderDAO = webOrderDAO;
    }

    public List<WebOrder> getOrders(LocalUser user){
        return webOrderDAO.findByUser(user);
    }
}
