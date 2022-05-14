package com.example.futuresmicroservice.jpa.entities

import com.fasterxml.jackson.annotation.JsonManagedReference
import org.apache.tomcat.jni.Local
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*


@Entity
@Table(name="result_table")
open class ResultEntity {

    constructor(_date : LocalDateTime, _idUser : UserEntity, _value : Double): this() {
        this.date = _date
        this.idUser = _idUser
        this.value = _value
    }

    constructor()

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    open var id: Long? = null

    @Column(name = "date", nullable = false)
    open var date: LocalDateTime? = null

    @OneToOne(cascade = [(CascadeType.ALL)])
    @JoinColumn(name = "idUser", referencedColumnName = "id")
    open var idUser: UserEntity? = null

    @Column(name = "value", nullable = false)
    open var value: Double? = null
}