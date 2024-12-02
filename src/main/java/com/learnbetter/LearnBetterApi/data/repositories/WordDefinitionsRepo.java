package com.learnbetter.LearnBetterApi.data.repositories;

import com.learnbetter.LearnBetterApi.data.db.DefinitionsTable;
import com.learnbetter.LearnBetterApi.data.db.WordDefPK;
import com.learnbetter.LearnBetterApi.data.db.WordDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordDefinitionsRepo extends JpaRepository<WordDefinition, WordDefPK> {

    List<WordDefinition> findByDefTable(DefinitionsTable defTable);
}
