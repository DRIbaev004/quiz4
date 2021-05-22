package itis.parsing;

import itis.parsing.annotations.FieldName;
import itis.parsing.annotations.MaxLength;
import itis.parsing.annotations.NotBlank;

import java.time.LocalDate;

//Вносить изменения в этот класс не нужно!
final class Park {

    @FieldName("title")
    private String legalName;

    @NotBlank
    @MaxLength(13)
    private String ownerOrganizationInn;

    @NotBlank
    private LocalDate foundationYear;

    private Park() {
    }

    @Override
    public String toString() {
        return "Park{" +
                "legalName='" + legalName + '\'' +
                ", ownerOrganizationInn='" + ownerOrganizationInn + '\'' +
                ", foundationYear=" + foundationYear +
                '}';
    }
}
