package com.carshiring.models;

import java.util.List;

/**
 * Created by Muhib.
 * Contact Number : +91 9796173066
 */
public class Category {



    /**
     * message : success
     * response : {"cat":[{"category_id":"1","category_name":"Mini","category_image":"New_Ford_Fiesta__Mini2.JPG","code":["1"]},{"category_id":"2","category_name":"Economic","category_image":"x.jpg","code":["3","34"]},{"category_id":"3","category_name":"Compect","category_image":"compect2.png","code":["2","4","35"]},{"category_id":"4","category_name":"Intermediate","category_image":"Toyota_corolla_Intermediate2.jpg","code":["5","25","36","22","6"]},{"category_id":"5","category_name":"Standard","category_image":"Camry_Standard2.png","code":["7","37"]},{"category_id":"6","category_name":"Full size","category_image":"full_size2.png","code":["8","38"]},{"category_id":"7","category_name":"Premium","category_image":"Premier_car.jpg","code":["10","23","39","40","9","32"]}]}
     * status : true
     */

    private String message;
    private ResponseBean response;
    private boolean status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResponseBean getResponse() {
        return response;
    }

    public void setResponse(ResponseBean response) {
        this.response = response;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public static class ResponseBean {
        private List<CatBean> cat;

        public List<CatBean> getCat() {
            return cat;
        }

        public void setCat(List<CatBean> cat) {
            this.cat = cat;
        }

        public static class CatBean {
            /**
             * category_id : 1
             * category_name : Mini
             * category_image : New_Ford_Fiesta__Mini2.JPG
             * code : ["1"]
             */

            private String category_id;
            private String category_name;
            private String category_image;
            private List<Integer> code;

            public String getCategory_id() {
                return category_id;
            }

            public void setCategory_id(String category_id) {
                this.category_id = category_id;
            }

            public String getCategory_name() {
                return category_name;
            }

            public void setCategory_name(String category_name) {
                this.category_name = category_name;
            }

            public String getCategory_image() {
                return category_image;
            }

            public void setCategory_image(String category_image) {
                this.category_image = category_image;
            }

            public List<Integer> getCode() {
                return code;
            }

            public void setCode(List<Integer> code) {
                this.code = code;
            }
        }
    }
}
