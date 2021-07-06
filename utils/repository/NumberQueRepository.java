/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.utils.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.go.tcra.lims.utils.entity.NumberQue;

import java.util.List;

/**
 * @author emmanuel.mfikwa
 */
@Repository
public interface NumberQueRepository extends JpaRepository<NumberQue, Long> {

    List<NumberQue> findTop10ByOrderByCreatedAtDesc();
}
