package team.solution.teham.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import team.solution.teham.api.models.ProcessInfo;

@Repository
public interface ProcessInfoRepository extends JpaRepository<ProcessInfo, Long> {
    
}
