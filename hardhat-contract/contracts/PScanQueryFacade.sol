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
    
    function erc20Info(IERC20Metadata erc20) public view returns (bool support20, string memory, string memory, uint8, uint256) {
        bool is20 = true;

        uint8 decimals = 0;
        try erc20.decimals() returns (uint8 result) {
            decimals = result;
        } catch {
            is20 = false;
        }

        string memory name = '';
        if(is20){
            try erc20.name() returns (string memory result) {
                name = result;
            } catch {
                is20 = false;
            }
        }

        string memory symbol = '';
        if(is20){
            try erc20.symbol() returns (string memory result) {
                symbol = result;
            } catch {
                is20 = false;
            }
        }

       uint256 totalSupply = 0;    
        if(is20){
            try erc20.totalSupply() returns (uint256 result) {
                totalSupply = result;
            } catch {
                is20 = false;
            }
        }
           
        return (is20, name, symbol, decimals, totalSupply); 
    }

    function erc721Info(address addr) public view returns (bool support165, bool support721, bool support721Metadata, bool support721Enumeration, string memory, string memory, uint256) {
        bool is165 = addr.supportsERC165();
        bool is721 = false;
        if(is165){
            is721 = addr.supportsInterface(IID_IERC721);
        }
        bool is721Metadata = false;
        bool is721Enumerable = false;
        if(is721){
            is721Metadata = addr.supportsInterface(IID_IERC721_METADATA);
            is721Enumerable = addr.supportsInterface(IID_IERC721_ENUMERABLE);  
        }
        string memory name = '';
        string memory symbol = '';
        if(is721Metadata){
            name = IERC721Metadata(addr).name();
            symbol = IERC721Metadata(addr).symbol();
        }
        uint256 totalSupply = 0;   
        if(is721Enumerable){
            totalSupply = IERC721Enumerable(addr).totalSupply();   
        }
        return (is165, is721, is721Metadata, is721Enumerable, name, symbol, totalSupply);
    }


    function erc1155Info(address addr) public view returns (bool support165, bool support1155, bool support1155Metadata) {
        bool is165 = addr.supportsERC165();
        bool is1155 = false;
        if(is165){
            is1155 = addr.supportsInterface(IID_IERC1155);
        }
        bool is1155Metadata = false;
        if(is1155){
            is1155Metadata = addr.supportsInterface(IID_IERC1155_METADATA);
        }
        return (is165, is1155, is1155Metadata);
    }


    // function erc721Info(IERC20Metadata erc20) public view returns (string memory, string memory, uint8, uint256) {
    //     string memory name = erc20.name();
    //     string memory symbol = erc20.symbol();
    //     uint256 totalSupply = erc20.totalSupply();        
    //     return (name, symbol, decimals, totalSupply); 
    // }

    // https://ethereum.stackexchange.com/questions/83991/how-do-i-check-for-the-existence-of-a-function-in-solidity-when-i-have-an-addres
}



