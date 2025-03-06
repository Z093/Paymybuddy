package com.example.paymybuddy.service;

import com.example.paymybuddy.model.User;
import com.example.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Logger;

@Service
public class UserFriendsService {

    private static final Logger logger = Logger.getLogger(UserFriendsService.class.getName());

    @Autowired
    private UserRepository userRepository; // Utilisation de UserRepository au lieu de UserFriendsRepository

    @Transactional
    public void addFriend(String userMail, String friendMail) {
        logger.info("🔍 Tentative d'ajout d'ami : " + userMail + " → " + friendMail);

        if (userMail.equals(friendMail)) {
            logger.warning("❌ L'utilisateur tente de s'ajouter lui-même !");
            throw new RuntimeException("Vous ne pouvez pas vous ajouter vous-même !");
        }

        User user = userRepository.findByMail(userMail)
                .orElseThrow(() -> {
                    logger.warning("❌ Utilisateur non trouvé : " + userMail);
                    return new RuntimeException("Utilisateur introuvable");
                });

        User friend = userRepository.findByMail(friendMail)
                .orElseThrow(() -> {
                    logger.warning("❌ Ami non trouvé : " + friendMail);
                    return new RuntimeException("Ami introuvable");
                });

        if (!user.getFriends().contains(friend)) {
            user.getFriends().add(friend);
            friend.getFriends().add(user); // Ajout des deux côtés
            userRepository.save(user);
            userRepository.save(friend);
            logger.info("✅ Ami ajouté avec succès : " + userMail + " ↔ " + friendMail);
        } else {
            logger.warning("⚠️ Cet utilisateur est déjà un ami !");
            throw new RuntimeException("Cet utilisateur est déjà votre ami");
        }
    }
}

