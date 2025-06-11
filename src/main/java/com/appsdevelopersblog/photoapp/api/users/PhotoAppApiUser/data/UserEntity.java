package com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Entity
@Table(name="users")
@Getter
@Setter
public class UserEntity implements Serializable {

    private static final long serialVersionUID = -2731425678149216053L;

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable=false, length=50)
    private String firstName;

    @Column(nullable=false, length=50)
    private String lastName;

/*
    When you annotate a field like:

    @Column(nullable = false, length = 120, unique = true)
    private String email;
    JPA/Hibernate translates this to a UNIQUE constraint on the column in the database. For example, in SQL, this would typically create:

    CREATE UNIQUE INDEX idx_users_email ON users(email);
    This means:

    1. The database engine uses a B-tree or hash index to maintain uniqueness.

    2. When inserting a new row, the DB quickly checks the index, not the entire table row-by-row.

    3. This ensures very fast lookups for uniqueness — O(log n) in B-tree-based indexes, not O(n).

*/

    @Column(nullable=false, length=120, unique=true)
    private String email;

    @Column(nullable=false, unique=true)
    private String userId;

    @Column(nullable=false, unique=true)
    private String encryptedPassword;
}
