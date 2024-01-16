package hcmute.kltn.vtv.service.user;

import hcmute.kltn.vtv.model.entity.user.RomChat;
import org.springframework.transaction.annotation.Transactional;

public interface IRomChatService {
    @Transactional
    void saveRomChat(String sender, String receiver);

    RomChat findRomChat(String sender, String receiver);
}
