package com.ecommerce.studentmarket.student.store.exceptions;

public class StoreAlreadyExistsException extends StoreException {
        public StoreAlreadyExistsException(String so) {
            super("Mã sở hữu gian hàng "+ so + " đã tồn tại.");
        }
}
