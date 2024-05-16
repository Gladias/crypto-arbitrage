import './App.css';
import binance from './assets/binance.svg';
import kraken from './assets/kraken.svg';
import kucoin from './assets/kucoin.svg';
import btc from './assets/btc.svg';
import eth from './assets/eth.svg';

function App() {
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
                  <h5>Ask price</h5>
                  <h5>Ask quantity</h5>
                </div>
                <div className="price-2">
                  <h5>Bid price</h5>
                  <h5>Bid quantity</h5>
                </div>
                <div className="price-3">
                  <h5>Ask and bid price spread</h5>
                  <h5>Volume for last 24 hours</h5>
                </div>
              </div>
              <div className="fees-titles">
                <div className="price-header">
                  <h3>Fees</h3>
                </div>
                <div className="fee-1">
                  <h5>Constant fee for maker</h5>
                  <h5>Constant fee for taker</h5>
                </div>
                <div className="fee-2">
                  <h5>Maker fee for current prices</h5>
                  <h5>Taker fee for current prices</h5>
                </div>
              </div>
            </div>
            <div className="data">
              <div className="price-titles">
                <div className="price-header">
                  <h3>-----</h3>
                </div>
                <div className="price-1">
                  <h5>123.312</h5>
                  <h5>123.312</h5>
                </div>
                <div className="price-2">
                  <h5>123.312</h5>
                  <h5>123.312</h5>
                </div>
                <div className="price-3">
                  <h5>123.312</h5>
                  <h5>123.312</h5>
                </div>
              </div>
              <div className="fees-titles">
                <div className="price-header">
                  <h3>-----</h3>
                </div>
                <div className="fee-1">
                  <h5>123.312</h5>
                  <h5>123.312</h5>
                </div>
                <div className="fee-2">
                  <h5>123.312</h5>
                  <h5>123.312</h5>
                </div>
              </div>
            </div>
            <div className="data">
              <div className="price-titles">
                <div className="price-header">
                  <h3>-----</h3>
                </div>
                <div className="price-1">
                  <h5>123.312</h5>
                  <h5>123.312</h5>
                </div>
                <div className="price-2">
                  <h5>123.312</h5>
                  <h5>123.312</h5>
                </div>
                <div className="price-3">
                  <h5>123.312</h5>
                  <h5>123.312</h5>
                </div>
              </div>
              <div className="fees-titles">
                <div className="price-header">
                  <h3>-----</h3>
                </div>
                <div className="fee-1">
                  <h5>123.312</h5>
                  <h5>123.312</h5>
                </div>
                <div className="fee-2">
                  <h5>123.312</h5>
                  <h5>123.312</h5>
                </div>
              </div>
            </div>
            <div className="data">
              <div className="price-titles">
                <div className="price-header">
                  <h3>-----</h3>
                </div>
                <div className="price-1">
                  <h5>123.312</h5>
                  <h5>123.312</h5>
                </div>
                <div className="price-2">
                  <h5>123.312</h5>
                  <h5>123.312</h5>
                </div>
                <div className="price-3">
                  <h5>123.312</h5>
                  <h5>123.312</h5>
                </div>
              </div>
              <div className="fees-titles">
                <div className="price-header">
                  <h3>-----</h3>
                </div>
                <div className="fee-1">
                  <h5>123.312</h5>
                  <h5>123.312</h5>
                </div>
                <div className="fee-2">
                  <h5>123.312</h5>
                  <h5>123.312</h5>
                </div>
              </div>
            </div>
          </div>
          <div className="bottom-section">
            <div className="bottom-area">
              <div className="arbitrage-1">
                <h5>Most valuable trade </h5>
                <p>Buy 1.00 BTC on KRAKEN and Sell 1.00 BTC on BINANCE with profit 0.73 USDT</p>
              </div>
              <div className="arbitrage-2">
                <h5>Second most valuable trade </h5>
                <p>Buy 1.00 BTC on KRAKEN and Sell 1.00 BTC on BINANCE with profit 0.73 USDT</p>
              </div>
              <div className="arbitrage-3">
                <h5>Third most valuable trade </h5>
                <p>Buy 1.00 BTC on KRAKEN and Sell 1.00 BTC on BINANCE with profit 0.73 USDT</p>
              </div>
            </div>
            <div className="settings-area">
              <h3>Settings</h3>
              <div className="coins">
                <img src={btc} alt='BTC Logo' />
                <img src={eth} alt='ETH Logo' />
              </div>
              <div className="fee">
                <h4>Fee Level</h4>
                <select name="feeLevel" id="feeLevelSelect">
                  <option value="REGULAR_USER">Regular User</option>
                  <option value="VIP_1">VIP 1</option>
                  <option value="VIP_1">VIP 2</option>
                  <option value="VIP_1">VIP 3</option>
                  <option value="VIP_1">VIP 4</option>
                  <option value="VIP_1">VIP 5</option>
                  <option value="VIP_1">VIP 6</option>
                  <option value="VIP_1">VIP 7</option>
                  <option value="VIP_1">VIP 8</option>
                  <option value="VIP_1">VIP 9</option>
                  <option value="VIP_1">NONE</option>
                </select>
              </div>
              <div className="refresh-rate">
                <h4>Refresh Rate</h4>
                <select name="feeLevel" id="feeLevelSelect">
                  <option value="5seconds">5s</option>
                  <option value="10seconds">10s</option>
                  <option value="20seconds">20s</option>
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
