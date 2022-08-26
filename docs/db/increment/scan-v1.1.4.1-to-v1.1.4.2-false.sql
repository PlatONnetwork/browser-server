USE `scan_platon`;
ALTER TABLE tx_bak DROP `input`;
ALTER TABLE tx_bak DROP `bin`;
ALTER TABLE tx_bak ADD INDEX idx_time ( `time` );