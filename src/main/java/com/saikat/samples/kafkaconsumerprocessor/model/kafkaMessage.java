package com.saikat.samples.kafkaconsumerprocessor.model;

import lombok.*;

/**
 * @author Saikat
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class kafkaMessage {

    String Id;
    String name;
    String message;
    String timestamp;
}
