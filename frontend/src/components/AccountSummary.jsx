import React from "react";

function AccountSummary({ totalBalance }) {
  const formattedBalance =
    typeof totalBalance === "number"
      ? totalBalance.toLocaleString("en-US", {
          style: "currency",
          currency: "CAD",
        })
      : "$0.00";

  return (
    <div className="container">
      <div className="row justify-content-center">
        <div className="col-md-8">
          <div className="card mb-4">
            <div className="card-body">
              <div className="d-flex justify-content-end align-items-center">
                <h4 className="card-title mb-0 me-3">Total:</h4>
                <span className="h4 mb-0 text-primary">{formattedBalance}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default AccountSummary;
