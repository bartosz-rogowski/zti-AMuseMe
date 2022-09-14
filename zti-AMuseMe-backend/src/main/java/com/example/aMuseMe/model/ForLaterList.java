package com.example.aMuseMe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "for_later_list")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForLaterList {

    @Id
    @SequenceGenerator(
            name = "for_later_list_sequence",
            sequenceName = "for_later_list_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "for_later_list_sequence"
    )
    private Long id;

    @ManyToOne
    private User user;

    private String albumMBID;
//    private LocalDate date;

}
