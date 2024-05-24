import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './App.css';
import binance from './assets/binance.svg';
import kraken from './assets/kraken.svg';
import kucoin from './assets/kucoin.svg';
import btc from './assets/btc.svg';
import eth from './assets/eth.svg';

function App() {
  const [data, setData] = useState(null);
  const [coin, setCoin] = useState("BTC");
  const [feeLevel, setFeeLevel] = useState("REGULAR_USER");
  const [intervalTime, setIntervalTime] = useState(10000);

  useEffect(() => {
    const fetchData = () => {
      axios.get('http://localhost:8080/api/markets' + '?coin=' + coin + '&feeLevel=' + feeLevel)
        .then(response => {
          setData(response.data);
          console.log(response.data);
          console.log(response.data.arbitrageOptions);
        });
    };

    fetchData(); // Initial fetch

    const intervalId = setInterval(fetchData, intervalTime);

    // Cleanup interval on component unmount
    return () => clearInterval(intervalId);
  }, [coin, feeLevel, intervalTime]);

  return (
    <div className="App">
      <div className="main">
        <div className="first-column">
          <div className="title">
            <h1>Crypto Arbitrage Tool</h1>
          </div>
          <div className="market-titles">
            <div className="market">
              <h2>Binance</h2>
            </div>
            <div className="market">
              <h2>Kraken</h2>
            </div>
            <div className="market">
              <h2>KuCoin</h2>
            </div>
          </div>
          <div className="logos-section">
            <div className="logo">
              <img src={binance} alt='Binance Logo' />
            </div>
            <div className="logo">
              <img src={kraken} alt='Kraken Logo' />
            </div>
            <div className="logo">
              <img src={kucoin} alt='KuCoin Logo' />
            </div>
          </div>
          <div className="data-section">
            <div className="data data-titles">
              <div className="price-titles">
                <div className="price-header">
                  <h3>Price</h3>
                </div>
                <div className="price-1">
                  <h4>Ask price</h4>
                  <h4>Ask quantity</h4>
                </div>
                <div className="price-2">
                  <h4>Bid price</h4>
                  <h4>Bid quantity</h4>
                </div>
                <div className="price-3">
                  <h4>Ask and bid price spread</h4>
                  <h4>Volume for last 24 hours</h4>
                </div>
              </div>
              <div className="fees-titles">
                <div className="price-header">
                  <h3>Fees</h3>
                </div>
                <div className="fee-1">
                  <h4>Constant fee for maker</h4>
                  <h4>Constant fee for taker</h4>
                </div>
                <div className="fee-2">
                  <h4>Taker fee for current ask price</h4>
                  <h4>Taker fee for current bid price</h4>
                </div>
              </div>
            </div>
            <div className="data">
              <div className="price-titles">
                <div className="price-header">
                  <h3>-----</h3>
                </div>
                <div className="price-1">
                  <h4>{data && data.markets[0].price.askPrice}</h4>
                  <h4>{data && data.markets[0].price.askQuantity}</h4>
                </div>
                <div className="price-2">
                  <h4>{data && data.markets[0].price.bidPrice}</h4>
                  <h4>{data && data.markets[0].price.bidQuantity}</h4>
                </div>
                <div className="price-3">
                  <h4>{data && data.markets[0].price.askAndBidPriceSpread}</h4>
                  <h4>{data && data.markets[0].volume.last24HrsVolume}</h4>
                </div>
              </div>
              <div className="fees-titles">
                <div className="price-header">
                  <h3>-----</h3>
                </div>
                <div className="fee-1">
                  <h4>{data && data.markets[0].fee.constantFeeForMaker}</h4>
                  <h4>{data && data.markets[0].fee.constantFeeForTaker}</h4>
                </div>
                <div className="fee-2">
                  <h4>{data && data.markets[0].fee.takerFeeForCurrentAskPrice}</h4>
                  <h4>{data && data.markets[0].fee.takerFeeForCurrentBidPrice}</h4>
                </div>
              </div>
            </div>
            <div className="data">
              <div className="price-titles">
                <div className="price-header">
                  <h3>-----</h3>
                </div>
                <div className="price-1">
                  <h4>{data && data.markets[1].price.askPrice}</h4>
                  <h4>{data && data.markets[1].price.askQuantity}</h4>
                </div>
                <div className="price-2">
                  <h4>{data && data.markets[1].price.bidPrice}</h4>
                  <h4>{data && data.markets[1].price.bidQuantity}</h4>
                </div>
                <div className="price-3">
                  <h4>{data && data.markets[1].price.askAndBidPriceSpread}</h4>
                  <h4>{data && data.markets[1].volume.last24HrsVolume}</h4>
                </div>
              </div>
              <div className="fees-titles">
                <div className="price-header">
                  <h3>-----</h3>
                </div>
                <div className="fee-1">
                  <h4>{data && data.markets[1].fee.constantFeeForMaker}</h4>
                  <h4>{data && data.markets[1].fee.constantFeeForTaker}</h4>
                </div>
                <div className="fee-2">
                  <h4>{data && data.markets[1].fee.takerFeeForCurrentAskPrice}</h4>
                  <h4>{data && data.markets[1].fee.takerFeeForCurrentBidPrice}</h4>
                </div>
              </div>
            </div>
            <div className="data">
              <div className="price-titles">
                <div className="price-header">
                  <h3>-----</h3>
                </div>
                <div className="price-1">
                  <h4>{data && data.markets[2].price.askPrice}</h4>
                  <h4>{data && data.markets[2].price.askQuantity}</h4>
                </div>
                <div className="price-2">
                  <h4>{data && data.markets[2].price.bidPrice}</h4>
                  <h4>{data && data.markets[2].price.bidQuantity}</h4>
                </div>
                <div className="price-3">
                  <h4>{data && data.markets[2].price.askAndBidPriceSpread}</h4>
                  <h4>{data && data.markets[2].volume.last24HrsVolume}</h4>
                </div>
              </div>
              <div className="fees-titles">
                <div className="price-header">
                  <h3>-----</h3>
                </div>
                <div className="fee-1">
                  <h4>{data && data.markets[2].fee.constantFeeForMaker}</h4>
                  <h4>{data && data.markets[2].fee.constantFeeForTaker}</h4>
                </div>
                <div className="fee-2">
                  <h4>{data && data.markets[2].fee.takerFeeForCurrentAskPrice}</h4>
                  <h4>{data && data.markets[2].fee.takerFeeForCurrentBidPrice}</h4>
                </div>
              </div>
            </div>
          </div>
          <div className="bottom-section">
            <div className="bottom-area">
              <div className="arbitrage-1">
                <h4>Most valuable trade </h4>
                <h4 className={data && data.arbitrageOptions[0].color}>{data && data.arbitrageOptions[0].description}</h4>
              </div>
              <div className="arbitrage-2">
                <h4>Second most valuable trade </h4>
                <h4 className={data && data.arbitrageOptions[0].color}>{data && data.arbitrageOptions[1].description}</h4>
              </div>
              <div className="arbitrage-3">
                <h4>Third most valuable trade </h4>
                <h4 className={data && data.arbitrageOptions[0].color}>{data && data.arbitrageOptions[2].description}</h4>
              </div>
            </div>
            <div className="settings-area">
              <h3>Settings</h3>
              <div className="coins">
                <img src={btc} alt='BTC Logo' onClick={() => setCoin("BTC")} />
                <img src={eth} alt='ETH Logo' onClick={() => setCoin("ETH")} />
              </div>
              <div className="fee">
                <h4>Fee Level</h4>
                <select name="feeLevel" id="feeLevelSelect" onChange={(event) => setFeeLevel(event.target.value)}>
                  <option value="REGULAR_USER">Regular User</option>
                  <option value="VIP_1">VIP 1</option>
                  <option value="VIP_2">VIP 2</option>
                  <option value="VIP_3">VIP 3</option>
                  <option value="VIP_4">VIP 4</option>
                  <option value="VIP_5">VIP 5</option>
                  <option value="VIP_6">VIP 6</option>
                  <option value="VIP_7">VIP 7</option>
                  <option value="VIP_8">VIP 8</option>
                  <option value="VIP_9">VIP 9</option>
                  <option value="NONE">NONE</option>
                </select>
              </div>
              <div className="refresh-rate">
                <h4>Refresh Rate</h4>
                <select name="feeLevel" id="feeLevelSelect" onChange={(event) => setIntervalTime(event.target.value)}>
                  <option value="1000">1s</option>
                  <option value="5000">5s</option>
                  <option selected value="10000">10s</option>
                  <option value="20000">20s</option>
                </select>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;
