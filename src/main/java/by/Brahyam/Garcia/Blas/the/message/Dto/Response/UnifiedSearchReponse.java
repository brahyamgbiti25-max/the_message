package by.Brahyam.Garcia.Blas.the.message.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UnifiedSearchReponse {
    private List<RoomsReponse> existingChats=new ArrayList<>();
    private List<UserReponse> newContacts=new ArrayList<>();
}
