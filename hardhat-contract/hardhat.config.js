require("@nomicfoundation/hardhat-toolbox");

module.exports = {
  solidity: "0.8.18",
  networks:{
    platon_dev: {
      url: `http://192.168.120.146:6789`,
      accounts: [
        '93fb742e13e5af9e8f3a264a578eeb843e5bf232244f585f3503cbbcc47fcd85', 
        '6aea8517460742cd737c24a208f509217bc1a5869b9a228039725272fd3bc4a7',
        '5f934b6e9d6d9093b2550df47245fbba4e925f340da557e70efaea639977e4d4'
      ]
    },

    platon_prod: {
      url: `https://openapi2.platon.network/rpc`,
      accounts: [
        '93fb742e13e5af9e8f3a264a578eeb843e5bf232244f585f3503cbbcc47fcd85'
      ]
    }
  }
};

