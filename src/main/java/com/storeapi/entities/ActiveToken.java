package com.storeapi.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "active_tokens")
public class ActiveToken {
  @Id
  @Column(name = "token")
  private UUID token;
}