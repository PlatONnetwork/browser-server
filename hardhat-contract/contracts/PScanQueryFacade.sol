// SPDX-License-Identifier: MIT
pragma solidity ^0.8.9;

import "@openzeppelin/contracts/token/ERC20/extensions/IERC20Metadata.sol";
import "@openzeppelin/contracts/token/ERC721/IERC721.sol";
import "@openzeppelin/contracts/token/ERC721/extensions/IERC721Metadata.sol";
import "@openzeppelin/contracts/token/ERC721/extensions/IERC721Enumerable.sol";
import "@openzeppelin/contracts/token/ERC1155/IERC1155.sol";
import "@openzeppelin/contracts/token/ERC1155/extensions/IERC1155MetadataURI.sol";
import "@openzeppelin/contracts/utils/introspection/ERC165Checker.sol";
import "@openzeppelin/contracts/utils/introspection/IERC165.sol";

contract PScanQueryFacade {

    using ERC165Checker for address;
    bytes4 public constant IID_IERC165 = type(IERC165).interfaceId;
    bytes4 public constant IID_IERC721 = type(IERC721).interfaceId;
    bytes4 public constant IID_IERC721_METADATA = type(IERC721Metadata).interfaceId;
    bytes4 public constant IID_IERC721_ENUMERABLE = type(IERC721Enumerable).interfaceId;
    bytes4 public constant IID_IERC1155 = type(IERC1155).interfaceId;
    bytes4 public constant IID_IERC1155_METADATA = type(IERC1155MetadataURI).interfaceId;


    function ercInfo(address addr) public view returns (uint8, string memory, string memory, uint8, uint256) {
        uint8 bitmap;
        string memory name;
        string memory symbol;
        uint8 decimals;
        uint256 totalSupply;

        (bitmap, name, symbol, totalSupply) = erc721Info(addr);
        if(bitmap & 4 == 4){
            return (bitmap, name, symbol, decimals, totalSupply);
        }

        (bitmap, name, symbol) = erc1155Info(addr);
        if(bitmap & 32 == 32){
            return (bitmap, name, symbol, decimals, totalSupply);
        }

        (bitmap, name, symbol, decimals, totalSupply) = erc20Info(IERC20Metadata(addr));
        return (bitmap, name, symbol, decimals,totalSupply);
    }


    function erc20Info(IERC20Metadata erc20) public view returns (uint8, string memory, string memory, uint8, uint256) {
        uint8 bitmap = 1;
        uint8 decimals = 0;
        try erc20.decimals() returns (uint8 result) {
            decimals = result;
        } catch {
            bitmap = 0;
        }

        string memory name = '';
        if(bitmap == 1){
            try erc20.name() returns (string memory result) {
                name = result;
            } catch {
                bitmap = 0;
            }
        }

        string memory symbol = '';
        if(bitmap == 1){
            try erc20.symbol() returns (string memory result) {
                symbol = result;
            } catch {
                bitmap = 0;
            }
        }

        uint256 totalSupply = 0;
        if(bitmap == 1){
            try erc20.totalSupply() returns (uint256 result) {
                totalSupply = result;
            } catch {
                bitmap = 0;
            }
        }

        return (bitmap, name, symbol, decimals, totalSupply);
    }

    function erc721Info(address addr) public view returns (uint8, string memory, string memory, uint256) {
        uint8 bitmap;
        bitmap = bitmap | (addr.supportsERC165() ? 2 : 0);

        if(bitmap & 2 == 2){
            bitmap = bitmap | (addr.supportsInterface(IID_IERC721) ? 4 : 0);
        }

        if(bitmap & 4 == 4){
            bitmap = bitmap | (addr.supportsInterface(IID_IERC721_METADATA) ? 8 : 0);
            bitmap = bitmap | (addr.supportsInterface(IID_IERC721_ENUMERABLE) ? 16 : 0);
        }
        string memory name = '';
        string memory symbol = '';
        if(bitmap & 8 == 8){
            name = IERC721Metadata(addr).name();
            symbol = IERC721Metadata(addr).symbol();
        }
        uint256 totalSupply = 0;
        if(bitmap & 16 == 16){
            totalSupply = IERC721Enumerable(addr).totalSupply();
        }
        return (bitmap, name, symbol, totalSupply);
    }


    function erc1155Info(address addr) public view returns (uint8, string memory, string memory) {
        uint8 bitmap;
        bitmap = bitmap | (addr.supportsERC165() ? 2 : 0);

        if(bitmap & 2 == 2){
            bitmap = bitmap | (addr.supportsInterface(IID_IERC1155) ? 32 : 0);
        }

        bool is1155 = bitmap & 32 == 32;
        if(is1155){
            bitmap = bitmap | (addr.supportsInterface(IID_IERC1155_METADATA) ? 64 : 0);
        }

        // 非标准检测, 因为scan目前存在此逻辑
        string memory name = '';
        if(is1155){
            try IERC20Metadata(addr).name() returns (string memory result) {
                name = result;
            } catch {

            }
        }

        // 非标准检测, 因为scan目前存在此逻辑
        string memory symbol = '';
        if(is1155){
            try IERC20Metadata(addr).symbol() returns (string memory result) {
                symbol = result;
            } catch {

            }
        }
        return (bitmap, name, symbol);
    }
}