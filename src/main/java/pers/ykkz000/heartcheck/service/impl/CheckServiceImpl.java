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

package pers.ykkz000.heartcheck.service.impl;

import org.apache.commons.math3.util.Pair;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerMinMaxScaler;
import org.nd4j.linalg.dataset.api.preprocessor.serializer.MinMaxSerializerStrategy;
import org.nd4j.linalg.dataset.api.preprocessor.serializer.NormalizerSerializer;
import org.nd4j.linalg.factory.Nd4j;
import pers.ykkz000.heartcheck.service.CheckService;
import pers.ykkz000.yukikaze.framework.annotation.Bean;
import pers.ykkz000.yukikaze.framework.annotation.BeanConstructor;

import java.util.Map;
import java.util.Objects;

@Bean(name = "checkService")
public class CheckServiceImpl implements CheckService {
    private final MultiLayerNetwork NETWORK;
    private final NormalizerMinMaxScaler NORMALIZER;

    @BeanConstructor
    public CheckServiceImpl() throws Exception {
        NormalizerSerializer normalizerSerializer = new NormalizerSerializer();
        normalizerSerializer.addStrategy(new MinMaxSerializerStrategy());
        NETWORK = ModelSerializer.restoreMultiLayerNetwork(Objects.requireNonNull(CheckServiceImpl.class.getResourceAsStream("/model/model.zip")), false);
        NORMALIZER = normalizerSerializer.restore(Objects.requireNonNull(CheckServiceImpl.class.getResourceAsStream("/model/preprocessor.zip")));
    }

    @Override
    public Pair<Double, Double> heartCheck(Map<String, Double> data) {
        double[] input = new double[14];
        for (String key : data.keySet()) {
            switch (key) {
                case "age" -> input[0] = data.get(key);
                case "sex" -> input[1] = data.get(key);
                case "cp" -> {
                    switch ((int) data.get(key).doubleValue()) {
                        case 1 -> input[2] = 1;
                        case 2 -> input[3] = 1;
                        case 3 -> input[4] = 1;
                        case 4 -> input[5] = 1;
                        default -> {}
                    }
                }
                case "trestbps" -> input[6] = data.get(key);
                case "chol" -> input[7] = data.get(key);
                case "fbs" -> input[8] = data.get(key);
                case "thalach" -> input[9] = data.get(key);
                case "exang" -> input[10] = data.get(key);
                case "thal" -> {
                    switch ((int) data.get(key).doubleValue()) {
                        case 0 -> input[11] = 1;
                        case 1 -> input[12] = 1;
                        case 2 -> input[13] = 1;
                        default -> {}
                    }
                }
                default -> {}
            }
        }
        try (INDArray inputMatrix = Nd4j.create(new double[][]{input})) {
            NORMALIZER.transform(inputMatrix);
            INDArray outputMatrix = NETWORK.output(inputMatrix);
            return new Pair<>(outputMatrix.getRow(0).getDouble(0), outputMatrix.getRow(0).getDouble(1));
        }
    }
}
