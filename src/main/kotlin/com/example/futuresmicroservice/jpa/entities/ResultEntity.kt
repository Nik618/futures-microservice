package com.example.futuresmicroservice.jpa.entities

import com.fasterxml.jackson.annotation.JsonManagedReference
import org.apache.tomcat.jni.Local
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*


@Entity
@Table(name="result_table")
open class ResultEntity {

    constructor(_date : String, _idUser : UserEntity, _value : Double, _currentPrice : Double, _lastPrice : Double, _count : Long): this() {
        this.date = _date
        this.idUser = _idUser
        this.value = _value
        this.currentPrice = _currentPrice
        this.lastPrice = _lastPrice
        this.count = _count
    }

    constructor()

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    open var id: Long? = null

    @Column(name = "date", nullable = false)
    open var date: String? = null

    @OneToOne(cascade = [(CascadeType.MERGE)])
    @JoinColumn(name = "idUser", referencedColumnName = "id")
    open var idUser: UserEntity? = null

    @Column(name = "value", nullable = false)
    open var value: Double? = null

    @Column(name = "lastPrice")
    open var lastPrice: Double? = null

    @Column(name = "currentPrice")
    open var currentPrice: Double? = null

    @Column(name = "count")
    open var count: Long? = null
}