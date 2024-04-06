/*
 * heart-check
 * Copyright (C) 2024  ykkz000
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package pers.ykkz000.heartcheck.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.math3.util.Pair;
import org.slf4j.LoggerFactory;
import pers.ykkz000.heartcheck.service.CheckService;
import pers.ykkz000.yukikaze.framework.annotation.*;

import java.util.Hashtable;
import java.util.Map;

@Bean(name = "heartCheckController", autoLoad = true)
@Controller
public class CheckController {
    private final CheckService checkService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeanConstructor
    private CheckController(@AutoWire("checkService") CheckService checkService) {
        this.checkService = checkService;
    }

    @BindCommand("test")
    private String msg(@ParamVariable("id") int id) {
        return "test" + id;
    }

    @BindCommand("heartCheck")
    private String check(@ParamVariable("age") int age, @ParamVariable("sex") int sex, @ParamVariable("cp") int cp, @ParamVariable("trestbps") double trestbps, @ParamVariable("chol") double chol, @ParamVariable("fbs") int fbs, @ParamVariable("thalach") double thalach, @ParamVariable("exang") int exang, @ParamVariable("thal") int thal) throws JsonProcessingException {
        Hashtable<String, Double> data = new Hashtable<>();
        data.put("age", (double) age);
        data.put("sex", (double) sex);
        data.put("cp", (double) cp);
        data.put("trestbps", trestbps);
        data.put("chol", chol);
        data.put("fbs", (double) fbs);
        data.put("thalach", thalach);
        data.put("exang", (double) exang);
        data.put("thal", (double) thal);
        Pair<Double, Double> pair = checkService.heartCheck(data);
        Map<String, Double> result = new Hashtable<>();
        result.put("Negative", pair.getFirst());
        result.put("Positive", pair.getSecond());
        return objectMapper.writeValueAsString(result);
    }
}
