package com.example.diplom.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class File {
    @Id
    private Long id;
    @Column
    private String filename;
    @Lob
    @Column
    private byte[] file;

    @Column
    private Long size;

    @Column
    private Date timeCreate;

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", filename='" + filename + '\'' +
                ", file=" + Arrays.toString(file) +
                ", size=" + size +
                ", timeCreate=" + timeCreate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        File file = (File) o;
        return Objects.equals(filename, file.filename);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filename);
    }
}
