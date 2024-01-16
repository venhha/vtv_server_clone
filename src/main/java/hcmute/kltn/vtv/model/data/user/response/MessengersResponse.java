package hcmute.kltn.vtv.model.data.user.response;

import hcmute.kltn.vtv.model.dto.user.MessengerDTO;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MessengersResponse extends ResponseAbstract {

    private String username;

    private int count;

    private Long romChatId;

    private List<MessengerDTO> messengerDTOs;
}
