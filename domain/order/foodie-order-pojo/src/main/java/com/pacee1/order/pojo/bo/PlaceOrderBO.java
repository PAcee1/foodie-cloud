package com.pacee1.order.pojo.bo;

import com.pacee1.pojo.ShopcartBO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>下单BO</p>
 *
 * @author : Pace
 * @date : 2020-10-22 11:26
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceOrderBO {

    private OrderBO orderBO;

    private List<ShopcartBO> shopcartList;
}
