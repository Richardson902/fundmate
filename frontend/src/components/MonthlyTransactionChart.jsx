import { Pie } from "react-chartjs-2";
import { Card } from "react-bootstrap";
import { Chart as ChartJS, ArcElement, Tooltip, Legend, Title } from "chart.js";
import { useTransactions } from "../contexts/TransactionContext";

ChartJS.register(ArcElement, Tooltip, Legend, Title);

function MonthlyTransactionChart() {
  const { transactions } = useTransactions();

  const currentDate = new Date();
  const currentMonth = currentDate.getMonth();
  const currentYear = currentDate.getFullYear();

  const monthlyTransactions = transactions.filter((transaction) => {
    const transDate = new Date(transaction.date);
    return (
      transDate.getMonth() === currentMonth &&
      transDate.getFullYear() === currentYear
    );
  });

  const income = monthlyTransactions
    .filter((t) => t.amount > 0)
    .reduce((sum, t) => sum + t.amount, 0);

  const expenses = monthlyTransactions
    .filter((t) => t.amount < 0)
    .reduce((sum, t) => sum + Math.abs(t.amount), 0);

  const chartData = {
    labels: ["Income", "Expenses"],
    datasets: [
      {
        data: [income, expenses],
        backgroundColor: ["#198754", "#dc3545"],
        borderColor: ["#198754", "#dc3545"],
        borderWidth: 1,
        cutout: "60%",
      },
    ],
  };

  const options = {
    responsive: true,
    plugins: {
      legend: {
        display: false,
      },
      title: {
        display: true,
        text: "This Month",
      },
    },
  };

  return (
    <Card className="mb-4">
      <Card.Body>
        <div className="row align-items-center">
          <div className="col-md-8">
            <div style={{ height: "300px" }}>
              <Pie data={chartData} options={options} />
            </div>
          </div>
          <div className="col-md-4">
            <div className="d-flex flex-column gap-4">
              <div className="text-center">
                <div className="text-success h4">${income.toFixed(2)}</div>
                <div className="text-muted">Income</div>
              </div>
              <div className="text-center">
                <div className="text-danger h4">${expenses.toFixed(2)}</div>
                <div className="text-muted">Expenses</div>
              </div>
              <div className="text-center">
                <div
                  className={`h4 ${
                    income - expenses >= 0 ? "text-success" : "text-danger"
                  }`}
                >
                  ${(income - expenses).toFixed(2)}
                </div>
                <div className="text-muted">Net</div>
              </div>
            </div>
          </div>
        </div>
      </Card.Body>
    </Card>
  );
}

export default MonthlyTransactionChart;
