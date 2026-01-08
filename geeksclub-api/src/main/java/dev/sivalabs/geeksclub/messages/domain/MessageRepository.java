package dev.sivalabs.geeksclub.messages.domain;

import org.springframework.data.jpa.repository.JpaRepository;

interface MessageRepository extends JpaRepository<MessageEntity, Long> {}
