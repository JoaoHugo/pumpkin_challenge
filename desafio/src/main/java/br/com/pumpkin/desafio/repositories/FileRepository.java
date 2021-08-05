package br.com.pumpkin.desafio.repositories;

import br.com.pumpkin.desafio.models.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
}
