import { useNavigate } from "react-router-dom";

function LandingPage() {
  const navigate = useNavigate();

  return (
    <div className="hero bg-base-200 min-h-screen">
      <div className="hero-content flex-col lg:flex-row">
        <img
          src="https://img.daisyui.com/images/stock/photo-1635805737707-575885ab0820.webp"
          className="max-w-sm rounded-lg shadow-2xl"
        />
        <div>
          <h1 className="text-5xl font-bold">combinationCHECK</h1>
          <p className="py-6">Einfach nur ein Text</p>
          <button
            className="btn btn-primary"
            onClick={() => navigate("/select")}
          >
            Get Started
          </button>
        </div>
      </div>
    </div>
  );
}

export default LandingPage;
