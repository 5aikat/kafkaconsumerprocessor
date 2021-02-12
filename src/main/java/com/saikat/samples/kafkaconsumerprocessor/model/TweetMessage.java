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
public class TweetMessage {

    private String user;
    private String message;
}
