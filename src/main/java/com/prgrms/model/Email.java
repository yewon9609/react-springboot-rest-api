package com.prgrms.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Objects;

public class Email {

    @NotNull(message = "email 은 필수값입니다.")
    @Size(min = 5, max = 50, message = "5글자 이상 50글자 이하를 입력해주세요")
    @Pattern(regexp = "\\b[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,4}\\b", message = "email 형식을 확인해주세요")
    private final String address;

    public Email(String address) {
        this.address = address;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Email email = (Email) o;
        return Objects.equals(address, email.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }

    @Override
    public String toString() {
        return "Email{" +
            "address='" + address + '\'' +
            '}';
    }

    public String getAddress() {
        return address;
    }
}
