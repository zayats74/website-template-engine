package com.example.websitetemplateengine.repository;

import com.example.websitetemplateengine.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TemplateRepository extends JpaRepository<Template, UUID> {

}
